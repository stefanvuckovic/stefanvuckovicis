semanticwebapp
==============
1. O projektu
Tema ovog projekta je kreiranje web aplikacije koja će omogućiti integraciju i pretragu podataka o knjigama. Naime, potrebno je preuzeti podatke o knjigama sa različitih izvora na webu i izvršiti njihovu integraciju u lokalni repozitorijum. Nakon preuzimanja podataka, potrebno ih je predstaviti korišćenjem odgovarajućeg RDF vokabulara (npr. schema:Book). Podatke treba smestiti u lokalni RDF repozitorijum (npr. Jena TDB). Potrebno je implementirati jednostavan interfejs za pretragu ovih podataka korišćenjem SPARQL upita koji će se izvršavati nad lokalnim RDF repozitorijumom.

Osnovne faze u razvoju aplikacije:
preuzimanje podataka sa različitih izvora na webu
parsiranje podataka i njihova integracija
predstavljanje podataka korišćenjem odgovarajućeg RDF vokabulara
čuvanje podataka u lokalni RDF repozitorijum
implementacija interfejsa za pretragu podataka

2. Domenski model
Nakon analize podataka koje pružaju odabrani izvori podataka, kao i podataka

3. Dizajn
4. Implementacija
Aplikacija je napisana u programskom jeziku Java. 

Prilikom realizacije web aplikacije korišćene su sledeće Java tehnologije:
-Java Server Faces (JSF) sa Primefaces bibliotekom - za realizaciju korisničkog interfejsa
JavaServer Faces (JSF) je okvir korišćen na serverskoj strani (server-side component
framework) za izradu korisničkog interfejsa (user interface - UI) u web aplikacijama koje se
zasnivaju na Java tehnologiji.
-EJB - za realizaciju poslovne logike aplikacije
Enterprise Java Beans su JavaEE server-side komponente koje se izvršavaju unutar EJB kontejnera
i učauruju poslovnu logiku JavaEE aplikacija. Ove komponente su skalabilne, transakcione,
višenitne i mogu im pristupiti više korisnika u isto vreme. 

Za parsiranje preuzetih podataka u JSON formatu korišćena je JSON.simple biblioteka koja omogućava veoma jednostavno parsiranje. Kao što se može videti u primeru ispod, potrebno je metodi parse JSONParser objekta proslediti JSON tekst kako bi se izvršilo parsiranje nakon čega se veoma jednostavno izvlače željeni podaci pomoću objekata JSONArray (ako je u pitanju niz) i JSONObject (ako je reč o JSON objektu).
    JSONParser parser = new JSONParser();
		Object obj = parser.parse(tekst);
		JSONObject jsonObject = (JSONObject) obj;
    JSONArray niz=(JSONArray) jsonObject.get("items");

Za parsiranje preuzetih podataka u XML formatu korišćena je dom4j. Dom4j je open source XML okvir (framework) za programski jezik Java koji omogućava čitanje, pisanje, navigaciju, kreiranje i modifikaciju XML dokumenata. U primeru ispod se može videti kako se XML dokument može parsirati, a zatim i kako se može vršiti navigacija kroz čvorove dokumenta i čitanje podataka.
Document document=DocumentHelper.parseText(tekst);
List list=document.selectNodes("//result");
Node node=(Node) list.get(0);
Node isbn=node.selectSingleNode("isbn13");
System.out.println(isbn.getText());


