#include <netinet/in.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <netdb.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <pthread.h>

#define streq(s1,s2)    \
   strcmp(s1,s2) == 0

typedef struct threadArgs {
	int socket;
} ThreadArgs;

int createServer(struct sockaddr* sa) {
	int sc = socket(PF_INET, SOCK_STREAM, 0);
	int err = bind(sc, sa, sizeof(struct sockaddr));
	if (err == -1) {
		return -1;
	}
	err = listen(sc, 5);
	if (err < 0) {
		return -1;
	}
	return sc;
}

int createConnection(int serverSocket, struct sockaddr* sa) {
	printf("Waiting for connection...\n");
	int s = sizeof(struct sockaddr);
	int sc = accept(serverSocket, sa, (socklen_t*)&s);

	if (sc == -1) {
		printf("Connection failed\n");
		exit(-1);
	} else {
		printf("Connection successful. Socket : %d\n", sc);
	}

	return sc;
}

int sendMess(int socket, char* query) {
	int nb = send(socket, query, strlen(query)+1, 0);
	if (nb == -1) {
		printf("Error : couldn't make request\n");
	} else {
		printf("Request successful. Size : %d chars\n", nb);
	}

	return nb;
}

char* readMess(int socket) {
	char* mess = (char*)malloc(sizeof(char));
	char c = 1;
	int i = 0;
	int out = 0;
	while(out == 0) {
		recv(socket, &c, 1, 0);
		if (c == '\0') {
			out = 1;
		} else {
			mess[i] = c;
			i++;
			mess = realloc(mess, (i+1)*sizeof(char));
		}
	}
	mess[i] = '\0';
	return mess;
}

int echo(int connect, char* msg) {
	char* response = (char*)malloc(sizeof(char)*(strlen(msg)+1));
	int i;
	for (i = 0; i < strlen(msg); i++) {
		response[i] = msg[strlen(msg)-1-i];
	}
	response[i+1] = '\0';
	return sendMess(connect, response);
}

void* threadFunction(void* args) {
	ThreadArgs targs = *(ThreadArgs*)args;

	int sentResponse = sendMess(targs.socket, "Connected, type 'x' to disconnect");

	if(sentResponse) {
		char* mess;
		while(1) {
			mess = readMess(targs.socket);
			if(streq(mess, "x")) {
				break;
			}
			echo(targs.socket, mess);
			printf("Received '%s'", mess);
		}
	}

	printf("Closing connection");

	close(targs.socket);
	pthread_exit(NULL);
}

int mainMultithreadServer() {
	struct sockaddr_in sa;
	sa.sin_family = AF_INET;
	sa.sin_port = 0;
	sa.sin_addr.s_addr = 0;

	int ssocket = createServer((struct sockaddr*)&sa);
	int sz = sizeof(sa);
	getsockname(ssocket, (struct sockaddr*)&sa, &sz);
	printf("PORT : %d\n", ntohs(sa.sin_port));
	printf("CTRL-C to terminate the server\n");

	int csocket;
	int* newSock;
	while((csocket = createConnection(ssocket, (struct sockaddr*)&sa))) {
		pthread_t sniffThread;
		newSock = malloc(sizeof *newSock);
		*newSock = csocket;

		ThreadArgs args = {
			*newSock
		};

		if(pthread_create(&sniffThread, NULL, threadFunction, (void*)&args) < 0) {
			printf("Could not create thread\n");
			return 1;
		}

		printf("Connection assigned\n");
	}

	if(csocket < 0) {
		printf("Connection Refused\n");
		return 1;
	}

	return 0;
}

int main(int argc, char const *argv[]) {
	return mainMultithreadServer();
}