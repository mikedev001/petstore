local build/test/deploy
-----------------------

1) start tomcat 7 (TOMCAT_ROOT/bin/startup.bat)

then in this folder:

2) mvn clean

3) mvn package

4) mvn tomcat7:deploy

5) In a browser: http://localhost:8080/PetstoreWebApp/ to test the webapp

6) shutdown tomcat 7 when finished (TOMCAT_ROOT/bin/shutdown.bat)


deploy on Bluemix
-----------------
Account Bluemix (http://bluemix.net/): (30 jours trial: 20 juillet -> 20 aout)
user: mickael.agugliaro@wanadoo.fr
password: Petst_re001@
question: couleur
réponse: bleu
Region: US South
Organisation: PetStoreWebApp
Espace: DemoPetStoreWebApp
Application: ei706petstore

1) mvn clean

2) mvn package

3) mvn cf:push

4) http://ei706petstore.mybluemix.net
