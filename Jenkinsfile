pipeline {
    agent any

    tools {
        // Menggunakan instalasi Maven & JDK yang telah dikonfigurasi di Global Tool Configuration Jenkins
        maven 'Maven_3.9' // Sesuaikan dengan nama Tool Maven di Jenkins Anda
        jdk 'JDK_21'      // Sesuaikan dengan nama Tool JDK di Jenkins Anda
    }

    environment {
        // Menginstruksikan Playwright untuk mendownload browser ke folder lokal jika diperlukan
        PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD = '0'
    }

    stages {
        stage('Checkout SCM') {
            steps {
                // Melakukan checkout source code dari repositori Git
                checkout scm
            }
        }

        stage('Build & Install Dependencies') {
            steps {
                echo 'Mengompilasi proyek dan mengunduh dependensi Maven...'
                // Menjalankan clean compile untuk memastikan semua dependensi terunduh dan kode dapat dikompilasi
                bat 'mvn clean compile'
            }
        }

        stage('Execute Playwright Tests') {
            steps {
                echo 'Menjalankan skenario otomatisasi pengujian...'
                // Menggunakan `catchError` agar kegagalan test tidak langsung menghentikan pipeline (build aborted),
                // sehingga tahapan publish report di bagian `post` tetap berjalan.
                catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                    bat 'mvn test'
                }
            }
        }
    }

    post {
        always {
            echo 'Pengarsipan hasil tes dan pembuatan laporan...'

            // 1. Mengarsipkan file Log dan Screenshot sebagai Jenkins Build Artifacts
            archiveArtifacts artifacts: 'logs/*.log, reports/screenshots/*.png, reports/ExtentReport.html', 
                             fingerprint: true, 
                             allowEmptyArchive: true

            // 2. Mempublikasikan HTML Extent Report menggunakan Jenkins HTML Publisher Plugin
            // Pastikan Anda telah menginstal plugin "HTML Publisher" di Jenkins.
            publishHTML([
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'reports',
                reportFiles: 'ExtentReport.html',
                reportName: 'Playwright Extent Report',
                reportTitles: 'Extent Report'
            ])
            
            // 3. Mempublikasikan hasil TestNG (opsional, jika menggunakan plugin TestNG/JUnit di Jenkins)
            // testNG(pattern: '**/target/surefire-reports/testng-results.xml')
        }
        
        success {
            echo 'Pipeline selesai dengan sukses! Semua tes berhasil dilewati.'
        }
        
        failure {
            echo 'Pipeline gagal! Terdapat test case yang error. Silakan periksa Extent Report atau logs artifact.'
        }
    }
}
