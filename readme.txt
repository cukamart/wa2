1) stiahnut http://dev.mysql.com/downloads/installer/
2) vybrat custom instalaciu a nainstalovat:
    a) MySqlServers - najnovsi MySQL Server
    b) Applications - MySQL Workbench
    c) MysQL Connectors - Connector/J

3) treba sa vysomarit z toho Workbench a treba si tam vytvorit usera, vytvorit schemu kde budu nase tabulky...
ked si pullnes project z gitu je tam application.properties: usera som nazval cukamart heslo 1111 schemu som nazval airlineservicedatamodel
4) ked otvoris workbench je tam default nejaky localhost connection. Ten otvor vlavo pod MANAGEMENT mas "Users and Privileges"
tam treba vytvorit noveho usera "cukamart" a dat mu vsetky prava na vsetky schemy... (v nasom pripade staci airlineservicedatamodel)

ked mas vytvorene user a schemu tak nic viac netreba cez http://localhost:8080/semestral-project-0.0.1-SNAPSHOT/fill by sa ti mali vsetky tabulky vygenerovat a naplnit....
 
5) stiahnut najnovsi wildfly (10.1.0.Final) http://wildfly.org/downloads/

- na testovanie REST ja pouzivam v chrome Advanced REST Client

- pokial nechces aby sa pri kazdom restarte serveru vymazala databaza zmen si v application.properties spring.jpa.hibernate.ddl-auto = create na spring.jpa.hibernate.ddl-auto = update

- meno heslo zatial mame provizorne user user .... potom sa to bude riesit na 2. checkpoint nieco sofistikovanejsie

sample data:
cez Advanced REST Client sa cez post daju vytvarat nove instancie napr.

vytvorenie rezervacie na let s ID1
{
"flight": {
"id": 1
},
"seats": 1,
"state": "NEW"
}

obdobne vytvorenie letu...
{
"from": {
"id": 1,
"name": "Rome",
"lat": 45.12,
"lon": 39.12
},
"to": {
"id": 2,
"name": "Prague",
"lat": 40.14,
"lon": 32.47
},
"dateOfDeparture": 1479394963990,
"distance": 459.07,
"price": 999.99,
"seats": 390,
"name": "flight2"
}


REST SECURITY
- 1) nakonfigurovat v jave
- 2) cez Advanced Rest Client treba ist na
http://localhost:8080/semestral-project-0.0.1-SNAPSHOT/login (POST metoda)
treba pridat DATA FORM username a password....
- 3) sme autentifikovany a mozme vyuzivat zabezpecene URLs
- 4) treba mat nainstalovany ARC cookie exchange a zapnute XHR v advanced rest client !!!