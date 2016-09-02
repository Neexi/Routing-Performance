COMP3331 group programming project

Author:

    Jiashu Chen z3411585 jche804
    Rudi Purnomo z3410682 rpur114

Includes:

    1. RoutingPerformance Folder
        All the java files needed for execution

    2. report.pdf
    3. Makefile
        for easy execution

Instruction:

    Easy procedure with Makefile:
        1. open up a command-line utility tool
        2. navigate the the current directory, which contains files specified in the "Includes:" section.
        3. type "make build" into the command-line
        4. copy the "topology.txt" and "workload.txt" into current directory
        5. base on the pramater specified the following
            "make run(1)_(2)_(3)"

            replace (1) with upper case letter "C" or "P" depending on virtual circuit network or virtual packet network
            replace (2) with "SHP", "SDP" or "LLP" depending on the network routing scheme
                        SHP: Shortest Hop Path
                        SDP: Shortest Distance Path
                        LLP: Least Loaded Path
            replace (3) with number between 1-5 as packet rate number

        6. optional
            type "make clean" to remove the compiled class file form step 3

    OR:
        1. perform step 1-3 from the "Easy procedure with Makefile" section
        2. type into the commandline
            java RoutingPerformance.RoutingPerformance_IO (CIRCUIT|PACKET) (SHP|SDP|LLP) "path to topology.txt" "path to workload.txt" (packet rate)