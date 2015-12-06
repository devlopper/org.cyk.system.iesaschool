set SYSTEMID=iesaschool
set GIT_DIR=H:\Repositories\source code\git\org\cyk\system\%SYSTEMID%\

set WORKSPACE_DIRECTORY=H:\Repositories\source code\git\org\cyk\system\iesaschool\
set GLASSFISH_DIRECTORY=H:\Virtual Machines\Shared\glassfish_4_0_production_iesa\
set BACKUP_DIRECTORY=H:\Backup\
set ARTIFACT_DIRECTORY=H:\Repositories\artifacts\maven\
set VERSION=0.0.1
set DOMAIN_NAME=mydomain
set DATABASE_NAME=cyk_%SYSTEMID%_db
set WAR_PATH=%ARTIFACT_DIRECTORY%org\cyk\system\%SYSTEMID%\ui\web\jsf\primefaces\%SYSTEMID%-ui-web-primefaces-app\%VERSION%\%SYSTEMID%-ui-web-primefaces-app-%VERSION%.war
set DATA_PATH="%GIT_DIR%%SYSTEMID%-business-impl-ejb\%DATABASE_NAME%.sql"

cls

rem mysql -u root -proot -e "drop schema %DATABASE_NAME%;create database %DATABASE_NAME%;"
rem cd %GIT_DIR%%SYSTEMID%-business-impl-ejb
rem call mvn -Dtest=CreateLiveDatabaseBusinessIT -Dttype=live test

rem cd %GIT_DIR%_pom
rem call mvn clean install -U -Dmaven.test.skip=true

cd %BACKUP_DIRECTORY%
for /f "delims=" %%a in ('wmic OS Get localdatetime  ^| find "."') do set dt=%%a
set datestamp=%dt:~0,8%
set timestamp=%dt:~8,6%
set YYYY=%dt:~0,4%
set MM=%dt:~4,2%
set DD=%dt:~6,2%
set HH=%dt:~8,2%
set Min=%dt:~10,2%
set Sec=%dt:~12,2%
set stamp=%YYYY%_%MM%_%DD%_%HH%_%Min%_%Sec%

mkdir %stamp%
set BACKUP_DIRECTORY=%BACKUP_DIRECTORY%%stamp%\

call copy /y %WAR_PATH% %BACKUP_DIRECTORY%%SYSTEMID%.war
call copy /y %DATA_PATH% %BACKUP_DIRECTORY%cyk_%SYSTEMID%_db.sql

cd %GLASSFISH_DIRECTORY%bin
call asadmin delete-domain %DOMAIN_NAME%
call rmdir "%GLASSFISH_DIRECTORY%domains\%DOMAIN_NAME%" /S /Q
call asadmin create-domain --nopassword=true %DOMAIN_NAME%
call asadmin start-domain %DOMAIN_NAME%
call asadmin deploy --name %SYSTEMID% --contextroot %SYSTEMID% %WAR_PATH%

echo DONE