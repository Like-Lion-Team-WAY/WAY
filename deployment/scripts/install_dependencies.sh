#!/bin/bash
# Install any necessary dependencies (e.g., Java)
echo "Install dependencies"

#!/bin/bash

# Update package index
sudo yum update -y

# Install OpenJDK 21 (Amazon Linux 2 example)
sudo yum install -y java-21-amazon-corretto-devel

# Verify Java installation
java -version
