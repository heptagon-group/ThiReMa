FROM ThiReMaRegistry2.azurecr.io/ubuntu-env

WORKDIR /home

COPY ./githubSsh/heptagonKey /root/.ssh/heptagonKey
COPY ./githubSsh/heptagonKey.pub /root/.ssh/heptagonKey.pub
COPY ./githubSsh/sshConfig /root/.ssh/config
COPY ./githubSsh/knownHosts /root/.ssh/known_hosts
COPY ./backend_entrypoint.sh .
COPY ./frontend_entrypoint.sh .
COPY ./hookServer.py .

RUN chmod 777 ./backend_entrypoint.sh ./frontend_entrypoint.sh
RUN chmod 600 /root/.ssh/config
