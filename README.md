semanticwebapp
==============
#1. O projektu
Tema ovog projekta je kreiranje web aplikacije koja će omogućiti integraciju i pretragu podataka o knjigama. Naime, potrebno je preuzeti podatke o knjigama sa različitih izvora na webu i izvršiti njihovu integraciju u lokalni repozitorijum. Nakon preuzimanja podataka, potrebno ih je predstaviti korišćenjem odgovarajućeg RDF vokabulara (npr. schema:Book). Podatke treba smestiti u lokalni RDF repozitorijum (npr. Jena TDB). Potrebno je implementirati jednostavan interfejs za pretragu ovih podataka korišćenjem SPARQL upita koji će se izvršavati nad lokalnim RDF repozitorijumom.

Osnovne faze u razvoju aplikacije:
* preuzimanje podataka sa različitih izvora na webu
* parsiranje podataka i njihova integracija
* predstavljanje podataka korišćenjem odgovarajućeg RDF vokabulara
* čuvanje podataka u lokalni RDF repozitorijum
* implementacija interfejsa za pretragu podataka

#2. Domenski model
Nakon analize podataka koje pružaju odabrani izvori podataka, kao i podataka koje podržava odabrani RDF vokabular kreiran je domenski model i prikazan je na slici ispod (Slika 1).


Klasa Book sadrži osnovne podatke o knjizi kao što su: isbn, naslov knjige, broj strana, datum objavljivanja, kratak opis knjige kao i podatke o autorima preko reference na klasu Person i izdavaču knjige preko reference na klasu Organization.

Klasa Person sadrži informaciju o imenu osobe.

Klasa Organization sadrži informaciju o imenu organizacije.

#3. Dizajn
#4. Implementacija
Aplikacija je napisana u programskom jeziku Java. 

Prilikom realizacije web aplikacije korišćene su sledeće Java tehnologije:

1. Java Server Faces (JSF) sa Primefaces bibliotekom - za realizaciju korisničkog interfejsa.
JavaServer Faces (JSF) je okvir korišćen na serverskoj strani (server-side component
framework) za izradu korisničkog interfejsa (user interface - UI) u web aplikacijama koje se
zasnivaju na Java tehnologiji. Jedna od najvećih prednosti JSF okvira je ta što nudi jasnu odvojenost između prezentacije i ponašanja sistema.

2. EJB - za realizaciju poslovne logike aplikacije.
Enterprise Java Beans su JavaEE server-side komponente koje se izvršavaju unutar EJB kontejnera
i učauruju poslovnu logiku JavaEE aplikacija. Ove komponente su skalabilne, transakcione,
višenitne i mogu im pristupiti više korisnika u isto vreme. Enterprise bean-ovi pojednostavljuju razvoj distribuiranih aplikacija iz sledecih razloga:
	* EJB kontejner obezbeđuje sistemske servise (npr.upravljanje transakcijama) enterprise
	bean-ovima, dok se programer bean-a moze koncentrisati na rešavanje poslovnih
	problema.
	* Bean-ovi, a ne klijenti, sadrže poslovnu logiku aplikacije, dok se programer klijenta može
	fokusirati na predstavljanje klijenta. Programer klijenta ne mora da piše kod rutina
	koje implementiraju poslovna pravila ili pristup bazama podataka.
	* Enterprise bean-ovi su prenosive komponente. Ove aplikacije se mogu izvršavati na
	bilo kojem kompatibilnom Java EE serveru.

Za parsiranje preuzetih podataka u JSON formatu korišćena je JSON.simple biblioteka koja omogućava veoma jednostavno parsiranje. Kao što se može videti u primeru ispod, potrebno je metodi parse JSONParser objekta proslediti JSON tekst kako bi se izvršilo parsiranje nakon čega se veoma jednostavno izvlače željeni podaci pomoću objekata JSONArray (ako je u pitanju niz) i JSONObject (ako je reč o JSON objektu).

```
    JSONParser parser = new JSONParser();
    Object obj = parser.parse(tekst);
    JSONObject jsonObject = (JSONObject) obj;
    JSONArray niz=(JSONArray) jsonObject.get("items");
```

Za parsiranje preuzetih podataka u XML formatu korišćena je dom4j. Dom4j je open source XML okvir (framework) za programski jezik Java koji omogućava čitanje, pisanje, navigaciju, kreiranje i modifikaciju XML dokumenata. U primeru ispod se može videti kako se XML dokument može parsirati, a zatim i kako se može vršiti navigacija kroz čvorove dokumenta i čitanje podataka.

```
    Document document=DocumentHelper.parseText(tekst);
    List list=document.selectNodes("//result");
    Node node=(Node) list.get(0);
    Node isbn=node.selectSingleNode("isbn13");
    System.out.println(isbn.getText());
```

Aplikacija koristi i Jenabean biblioteku, koja služi za mapiranje Java objekata u RDF triplete. Ovo mapiranje se vrši korišćenjem anotacija. Jenabean omogućava:
* predstavljanje Java objekata preko RDF tripleta
* vezuje atribute objekata za RDF propertije
* čuva relacije ka drugim objektima
* omogućava ponovno učitavanje objekata.

Ispod je dat primer mapiranja korišćenjem anotacija:

```
    @Namespace(Constants.SCHEMA)
    @RdfType("Book")
    public class Book extends Thing{
    	    @RdfProperty(Constants.SCHEMA+"isbn")
    	    private String isbn;
	    @RdfProperty(Constants.SCHEMA+"name")
	    private String title;
	    @RdfProperty(Constants.SCHEMA+"numberOfPages")
	    private int numberOfPages;
	    @RdfProperty(Constants.SCHEMA+"author")
	    private List<Person> authors;
	    @RdfProperty(Constants.SCHEMA+"publisher")
	    private Organization publisher;
	    @RdfProperty(Constants.SCHEMA+"datePublished")
	    private Date datePublished;
	    @RdfProperty(Constants.SCHEMA+"description")
	    private String description;
	    ...
    }
```
        
Prilikom razvoja aplikacije korišćena je i Jena TDB biblioteka. Jena TDB je biblioteka koja se koristi za perzistenciju, odnosno čuvanje podataka u RDF repozitorijumu.

Kako bi se omogućila pretraga podataka korišćen je SPARQL. SPARQL je RDF upitni jezik pomoću koga je moguće čitati i manipulisati podacima u RDF formatu. 

#5. Priznanja
Ova aplikacija je razvijena za potrebe seminarskog rada iz predmeta Inteligentni sistemi na Fakultetu organizacionih nauka, Univerzitet u Beogradu, Srbija.



