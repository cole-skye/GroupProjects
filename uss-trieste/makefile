all: full_buildscript_Process
	@chmod +x build_script.sh

compile:
	@echo "Compiling Project"
	@./build_script.sh clean_compile_project
	@echo "Project Compiled"
	@echo "-------------------------"

package:
	@echo "Packaging Project"
	@./build_script.sh package_project
	@echo "Project Packaged"
	@echo "------------------------"

test-project:
	@echo "Starting Project server"
	@./build_script.sh project_tests
	@echo "Ended Project Server"
	@echo "------------------------"

run-api:
	@echo "Starting API server"
	@./build_script.sh run_api_server

kill-containers:
	@echo "Stopping all used containers"
	@./build_script.sh kill_used_containers
	@echo "Containers killed"

full_buildscript_Process:
	@echo "Compiling Project"
	@./build_script.sh compile_project
	@echo "Project Compiled"
	@echo "-------------------------"

	@echo "Packaged Project"
	@./build_script.sh package_project
	@echo "Project Packaged"
	@echo "------------------------"

	@echo "Starting Project server"
	@./build_script.sh project_tests
	@echo "Ended Project Server"
