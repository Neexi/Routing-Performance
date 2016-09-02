build:
	javac RoutingPerformance/RoutingPerformance_IO.java

clean:
	find . -name "*class" -type f -delete

reportbuild:
	pdflatex report.tex

reportclean:
	find . -name '*.aux' -type f -delete
	find . -name '*.toc' -type f -delete
	find . -name '*.log' -type f -delete
	find . -name '*.out' -type f -delete
	find . -name '*synctex*' -type f -delete

reportdestroy:
	rm report.pdf
	$(MAKE) reportclean

runC_SHP_1:
	java RoutingPerformance/RoutingPerformance_IO CIRCUIT SHP "./topology.txt" "./workload.txt" 1

runC_SDP_1:
	java RoutingPerformance/RoutingPerformance_IO CIRCUIT SDP "./topology.txt" "./workload.txt" 1

runC_LLP_1:
	java RoutingPerformance/RoutingPerformance_IO CIRCUIT LLP "./topology.txt" "./workload.txt" 1

runP_SHP_1:
	java RoutingPerformance/RoutingPerformance_IO PACKET SHP "./topology.txt" "./workload.txt" 1

runP_SDP_1:
	java RoutingPerformance/RoutingPerformance_IO PACKET SDP "./topology.txt" "./workload.txt" 1

runP_LLP_1:
	java RoutingPerformance/RoutingPerformance_IO PACKET LLP "./topology.txt" "./workload.txt" 1

runC_SHP_2:
	java RoutingPerformance/RoutingPerformance_IO CIRCUIT SHP "./topology.txt" "./workload.txt" 2

runC_SDP_2:
	java RoutingPerformance/RoutingPerformance_IO CIRCUIT SDP "./topology.txt" "./workload.txt" 2

runC_LLP_2:
	java RoutingPerformance/RoutingPerformance_IO CIRCUIT LLP "./topology.txt" "./workload.txt" 2

runP_SHP_2:
	java RoutingPerformance/RoutingPerformance_IO PACKET SHP "./topology.txt" "./workload.txt" 2

runP_SDP_2:
	java RoutingPerformance/RoutingPerformance_IO PACKET SDP "./topology.txt" "./workload.txt" 2

runP_LLP_2:
	java RoutingPerformance/RoutingPerformance_IO PACKET LLP "./topology.txt" "./workload.txt" 2

runC_SHP_3:
	java RoutingPerformance/RoutingPerformance_IO CIRCUIT SHP "./topology.txt" "./workload.txt" 3

runC_SDP_3:
	java RoutingPerformance/RoutingPerformance_IO CIRCUIT SDP "./topology.txt" "./workload.txt" 3

runC_LLP_3:
	java RoutingPerformance/RoutingPerformance_IO CIRCUIT LLP "./topology.txt" "./workload.txt" 3

runP_SHP_3:
	java RoutingPerformance/RoutingPerformance_IO PACKET SHP "./topology.txt" "./workload.txt" 3

runP_SDP_3:
	java RoutingPerformance/RoutingPerformance_IO PACKET SDP "./topology.txt" "./workload.txt" 3

runP_LLP_3:
	java RoutingPerformance/RoutingPerformance_IO PACKET LLP "./topology.txt" "./workload.txt" 3

runC_SHP_4:
	java RoutingPerformance/RoutingPerformance_IO CIRCUIT SHP "./topology.txt" "./workload.txt" 4

runC_SDP_4:
	java RoutingPerformance/RoutingPerformance_IO CIRCUIT SDP "./topology.txt" "./workload.txt" 4

runC_LLP_4:
	java RoutingPerformance/RoutingPerformance_IO CIRCUIT LLP "./topology.txt" "./workload.txt" 4

runP_SHP_4:
	java RoutingPerformance/RoutingPerformance_IO PACKET SHP "./topology.txt" "./workload.txt" 4

runP_SDP_4:
	java RoutingPerformance/RoutingPerformance_IO PACKET SDP "./topology.txt" "./workload.txt" 4

runP_LLP_4:
	java RoutingPerformance/RoutingPerformance_IO PACKET LLP "./topology.txt" "./workload.txt" 4

runC_SHP_5:
	java RoutingPerformance/RoutingPerformance_IO CIRCUIT SHP "./topology.txt" "./workload.txt" 5

runC_SDP_5:
	java RoutingPerformance/RoutingPerformance_IO CIRCUIT SDP "./topology.txt" "./workload.txt" 5

runC_LLP_5:
	java RoutingPerformance/RoutingPerformance_IO CIRCUIT LLP "./topology.txt" "./workload.txt" 5

runP_SHP_5:
	java RoutingPerformance/RoutingPerformance_IO PACKET SHP "./topology.txt" "./workload.txt" 5

runP_SDP_5:
	java RoutingPerformance/RoutingPerformance_IO PACKET SDP "./topology.txt" "./workload.txt" 5

runP_LLP_5:
	java RoutingPerformance/RoutingPerformance_IO PACKET LLP "./topology.txt" "./workload.txt" 5