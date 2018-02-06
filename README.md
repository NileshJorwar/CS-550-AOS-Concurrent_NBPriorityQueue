# CS 550 Advanced Operating System Project
We present an efficient and practical lock-free implementation of a concurrent priority queue that is suitable for both fully <br>
concurrent (large multi-processor) systems as well as pre-emptive (multi-process) systems. Many algorithms for concurrent priority <br>
queues are based on mutual exclusion. However, mutual exclusion causes blocking which has several drawbacks and degrades the system’s <br>
overall performance. Non-blocking algorithms avoid blocking, and are either lock-free or wait-free. <br>
Previously known non-blocking algorithms of priority queues did not perform well in practice because of <br>
their complexity, and they are often based on non-available atomic synchronization primitives. <br>
We have implemented our non-blocking concurrent priority queue in client-server architecture and <br> 
a real-time extension of our work is also described. In our performance evaluation we compare our model <br>
with some of the most efficient implementations of priority queues known.

## User Manual: 
Step 1. Unzip the project and look for the config.properties file in com/server folder to run the no of concurrent clients. 
Step 2. Enter the no of Concurrent clients to run in the config.properties file 
Step 3. Navigate to the folder "src" and run the batch file created named as "ServerRun" provided that port no is 9000. 
Step 4. Navigate to the "src" folder and run the Concurrent clients from the batch files created named as "ClientRun, ClientRun1" depending upon the no of concurrent clients entered in config.properties file. If noOfConcurrentClients are 2 then run the 2 concurrent batch files of the clients. Note: 1. These clients can also be run from the other systems provided that "IP address of the host" to be changed in the ClientChatForm.java 2. Priority of the clients can be changed from the batch files created.
Step 5. After the clients are run, enter the synchronous message from the interface opened to the clients and send. 
Step 6. Look if the server gives the interface to interact with higher priority client. 
Step 7. Enter the message that is sent to the higher priority client.