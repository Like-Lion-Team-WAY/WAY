pipeline {
    agent any
    
    environment {
        AWS_DEFAULT_REGION = 'ap-northeast-2'
        S3_BUCKET = 'likelionway-image'
        JAR_FILE = 'build/libs/WAY-0.0.1-SNAPSHOT.jar'
        APP_NAME = 'test_deploy'
        DEPLOY_GROUP = 'test_deploy_group'
        DEPLOY_ZIP = 'deployment.zip'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Like-Lion-Team-WAY/WAY.git', credentialsId: 'hohyeon'
            }
        }

        stage('Build') {
            steps {
                echo 'Building...'
                sh 'chmod 755 ./gradlew'
                sh './gradlew build -x test'
            }
        }

        stage('Prepare Deployment Package') {
            steps {
                echo 'Preparing deployment package...'
                sh """
                # Remove any existing ZIP file
                rm -f ${env.DEPLOY_ZIP}
                
                # Create the necessary directory structure for the ZIP file
                mkdir -p deployment/build/libs
                
                # Copy the JAR file to the correct location
                cp build/libs/WAY-0.0.1-SNAPSHOT.jar deployment/build/libs/
                
                # Ensure appspec.yml and scripts are in the deployment directory
                if [ ! -f deployment/appspec.yml ] || [ ! -d deployment/scripts ]; then
                    echo "Missing appspec.yml or scripts directory!"
                    exit 1
                fi
                
                # Change to the deployment directory and create the ZIP file
                cd deployment
                
                # Create the ZIP file with the required structure
                zip -r ../${env.DEPLOY_ZIP} build appspec.yml scripts
                
                # Verify the ZIP file content
                cd ..
                unzip -l ${env.DEPLOY_ZIP}
                """
            }
        }

        stage('Upload to S3') {
            steps {
                withAWS(credentials: 'way_hohyeon') {
                    script {
                        try {
                            s3Upload(bucket: env.S3_BUCKET, file: "${env.WORKSPACE}/${env.DEPLOY_ZIP}", path: "${env.DEPLOY_ZIP}")
                        } catch (Exception e) {
                            echo "Error uploading to S3: ${e.message}"
                            throw e
                        }
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                withAWS(credentials: 'way_hohyeon') {
                    sh """
                    aws deploy create-deployment \
                        --application-name ${env.APP_NAME} \
                        --deployment-group-name ${env.DEPLOY_GROUP} \
                        --s3-location bucket=${env.S3_BUCKET},key=${env.DEPLOY_ZIP},bundleType=zip
                    """
                }
            }
        }
    }
}
