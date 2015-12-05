set SYSTEMID=iesaschool

set GLASSFISH_DIRECTORY=H:\Virtual Machines\Shared\glassfish_4_0_production_iesa\
set ARTIFACT_DIRECTORY=H:\Repositories\artifacts\maven\
set VERSION=0.0.1
set DOMAIN_NAME=mydomain
set WAR_NAME=%SYSTEMID%
set WAR_PATH=%ARTIFACT_DIRECTORY%org\cyk\system\%SYSTEMID%\ui\web\jsf\primefaces\%SYSTEMID%-ui-web-primefaces-app\%VERSION%\%SYSTEMID%-ui-web-primefaces-app-%VERSION%.war

cls 

echo SYSTEM %SYSTEMID% WILL BE SETUP ON YOUR GLASSFISH SERVER AT LOCATION %GLASSFISH_DIRECTORY%

cd %GLASSFISH_DIRECTORY%bin

echo DELETE DOMAIN
call asadmin delete-domain %DOMAIN_NAME%

echo CREATE DOMAIN
call asadmin create-domain --nopassword=true %DOMAIN_NAME%

echo START DOMAIN
call asadmin start-domain %DOMAIN_NAME%

echo DEPLOY WAR
call asadmin deploy --name %WAR_NAME% --contextroot %WAR_NAME% %WAR_PATH%