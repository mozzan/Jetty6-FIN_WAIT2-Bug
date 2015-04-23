# Jetty6-FIN_WAIT2-Bug

This project provides a method to reproduce the bug within jetty server (6.x).

The bug is, if a connection is closed by remote without closing the socket, jetty will remain the connection with
FIN_WAIT2 state. Therefore, after a period of time, jetty will result in crash with too many files exceptions.

This project only contains the test. However, people who want to solve this problem can upgrade jetty to 7.6, 
which seems to have fixed this issue.

Test Usage :

java -jar Jetty6FINBug_tester.jar URL Connection_Number

ex : 
java -jar Jetty6FINBug_tester.jar 127.0.0.1:8888 100

Observation in linux :
netstat | grep FIN_WAIT2

The FIN_WAIT2 connections will be eliminated in 6 minutes with 7.6 jetty.




