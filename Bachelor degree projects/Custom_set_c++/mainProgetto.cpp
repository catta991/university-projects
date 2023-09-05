#include <iostream>
#include "set.h"
#include <string>




/* ***** inizio definizione strutture ed elementi di comodo ******** */

/**
  @brief Funtore di uguaglianza tra tipi interi 

  Verifica se 2 interi sono identici. 
*/

struct int_equal {
  bool operator()(int a, int b) const {
    return a==b;
  }
};


/**
  @brief Funtore predicato 

  Ritorna true se il valore intero passato è pari 
*/
struct is_even {
  bool operator()(int a) const {
    return (a % 2 ==0);
  }
};


/**
  Struct point che implementa un punto 2D.
  Si assume che un punto creato di default abbia coordinate 0 e 0

  @brief Struct point che implementa un punto 2D.
*/
struct point {
  int x; ///< coordinata x del punto
  int y; ///< coordinata y del punto

  point() : x(0), y(0) {} ///< cdefault
  
  point(int xx, int yy) : x(xx), y(yy) {} ///<csecondario

  point(const point &other): x(other.x), y(other.y){} ///<cconstructor

  point& operator=(const point &other){ ///<ridefinizione operatore = per punti
    if(this!=&other) {
      point temp(other);
      std::swap(this->x, temp.x);
      std::swap(this->y, temp.y);     
    }
    return *this;
  }


  ~point(){}


};


/**
  Funtore per il confronto di uguaglianza tra due punti. 
  Ritorna true se p1.x == p2.x e p1.y == p2.y

  @brief Funtore per il confronto di due punti.
*/
struct equal_point {
  bool operator()(const point &p1, const point &p2) const {
    return (p1.x==p2.x) && (p1.y==p2.y);
  } 
};

/**
  Ridefinizione dell'operatore di stream << per un punto.
  Necessario per l'operatore di stream della classe set.
*/
std::ostream &operator<<(std::ostream &os, const point &p) {
  std::cout<<"("<<p.x<<","<<p.y<<")";
  return os;
}

/**
  @brief Funtore che verifica se un punto è pari

  Ritorna true se il valore intero passat è pari 
*/
struct is_even_point {
  bool operator()(const point &p1) const {
    return (p1.x % 2 ==0) &&  (p1.y % 2 ==0);
  }
};




/**
  Struct person che implementa una persona.
  Si assume che il costruttore di default crei una persona senza 
  nome e cognome e con età pari a -1

  @brief Struct person che implementa una persona.
*/
struct person {
  std::string name; ///< nome della persona
  std::string surname; ///< cognome della persona
  int age; ///< età della persona

  person (): name(""), surname(""), age(-1) {} ///< cdefault

  person(std::string nm, std::string snm, int ag) : name(nm), surname(snm), age(ag) {} ///<csecondario


  person(const person &other): name(other.name), surname(other.surname),
  age(other.age) {} ///<cconstructor

  person& operator=(const person &other){ ///<ridefinizione operatore = per persone
    if(this!=&other) {
      
      person temp(other);
      std::swap(this->name, temp.name);
      std::swap(this->surname, temp.surname); 
      std::swap(this->age, temp.age);       
    }
    return *this;
  }


  ~person(){}///<distruttore




};


/**
  Funtore per il confronto di uguaglianza tra due persone. 
  Ritorna true se pers1.name == pers2.name e pers1.surname == pers2.surname e
  pers1.age == pers2.age

  @brief Funtore per il confronto di due persone.
*/
struct equal_person {
  bool operator()(const person &pers1, const person &pers2) const {
    return (pers1.name==pers2.name) && (pers1.surname==pers2.surname) && 
    (pers1.age == pers2.age);
  } 
};


/**
  Ridefinizione dell'operatore di stream << per una persona.
  Necessario per l'operatore di stream della classe set.
*/
std::ostream &operator<<(std::ostream &os, const person &pers) {
  std::cout<<"("<<pers.name<<","<<pers.surname<<","<<pers.age<<")";
  return os;
}


/**
  @brief Funtore che determina se una persona è maggiorenne (età >= 18)

  Ritorna true se la persona è maggiorenne
*/
struct is_adult {
  bool operator()(const person &pers) const {
    return (pers.age >= 18);
  }
};




///< Typedef di comodo per la creazione di set di interi, persone e punti

typedef set<int, int_equal> setInt;

typedef set<point, equal_point> setPoint;

typedef set<person, equal_person> setPerson;


/* ***** fine definizione strutture ed elementi di comodo ******** */





/*   ****** inizio test su interi ****** */


/**
  Test dei metodi fondamentali su set di interi

  @brief Test dei metodi fondamentali su set di interi
*/
void test_metodi_fondamentali_interi() {
  std::cout<<"******** Test metodi fondamentali su un set di interi ********"<<std::endl;

   setInt si;  // ctor default

    std::cout << "size del set creato dal costruttore di default : " << si.getSetSize() << std::endl;

   std::cout << "stampa del set creato con il costruttore di default" << std::endl;
   std::cout << si << std::endl;

   si.add(5); // add find
   si.add(8);
   si.add(1);
   si.add(4);

  // controllo su inserimento elementi duplicati
   si.add(1); 
   si.add(5);

   std::cout << "Stampa di si dopo inserimenti:" << std::endl;
   std::cout << si << std::endl;  // operator<<

   std::cout << "Stampa la dimensione di si " << std::endl;

   std::cout << si.getSetSize() << std::endl;



   setInt si2(si);  // cctor

   std::cout << "Stampa di si2 dopo copy constructor:" << std::endl;
   std::cout << si2 << std::endl;  // operator<<

   setInt si3;

   si3 = si; // operator=

   std::cout << "Stampa di si3 dopo assegnamento:" << std::endl;
   std::cout << si3 << std::endl;  // operator<<

   
   std::cout<< "copy constructor su set vuoto" << std::endl;
   setInt si4;

   setInt si5(si4);

   std::cout << "stampa del contenuto del set si5" << std::endl;
   std::cout << si5 << std::endl;

   std::cout << "stampa size si5" << std::endl;
   std::cout << si5.getSetSize() << std::endl;

   std::cout<< "operatore assegnamento su set vuoti" << std::endl;

   setInt si6;

   si6=si5;

   std::cout << "stampa del contenuto del set si6" << std::endl;
   std::cout << si6 << std::endl;

   std::cout << "stampa size si6" << std::endl;
   std::cout << si6.getSetSize() << std::endl;

   std::cout << "Stampa di si dopo rimozione elemento di testa" << std::endl;
   	
   si.remove(5);

   std::cout << si << std::endl;


	std::cout << "Stampa di si dopo rimozione elemento di coda" << std::endl;
   	
	si.remove(4);

  std::cout << si << std::endl;


	std::cout << "Stampa di si dopo rimozione elemento in centro" << std::endl;
   	
	si.remove(8);

  std::cout << si << std::endl;


  std::cout << "Stampa di si dopo rimozione elemento non presente" << std::endl;
   	
	si.remove(20);

  std::cout << si << std::endl;


  std::cout << "Stampa la dimensione di si dopo rimozioni " << std::endl;

  std::cout << si.getSetSize() << std::endl;







} 

// ------------------------------------------------------------------------------

	
	/**
  Test operatore[], operatore == 

  @brief Test operatore[], operatore == 
*/


void test_altri_metodi_su_interi(void){

	std::cout<<"******** Test altri metodi su un set di interi ********"<<std::endl;

	bool uguali = false;

	setInt si1;  // ctor default

  si1.add(5); // add find
  si1.add(8);
  si1.add(1);
  si1.add(4);

  try{

  std::cout <<"stampa il valore del primo elemento " << std::endl;
  std::cout << si1[0] << std::endl;


  std::cout <<"stampa il valore del secondo elemento " << std::endl;
  std::cout << si1[1] << std::endl;


  std::cout <<"stampa il valore dell'ultimo elemento " << std::endl;
  std::cout << si1[3] << std::endl;

  std::cout <<"controllo eccezione con indice negativo " << std::endl;
  std::cout << si1[-5] << std::endl;


 
 std::cout <<"stampa il valore di un elemento non presente " << std::endl;
 std::cout << si1[20] << std::endl;

std::cout <<"stampa il valore di un elemento di un set vuoto " << std::endl;
setInt sv;
std::cout << sv[20] << std::endl;


}catch (const std::out_of_range& ex) {
    std::cerr << "Out of Range error: " << ex.what() << std::endl;
  }


  setInt si2;  // ctor default

  si2.add(5); 
  si2.add(8);


 std::cout <<"stampa confronto tra si1 e si2 " << std::endl;
 uguali = si2 == si1; 
 std::cout << uguali << std::endl;



setInt si3;
 si3.add(1); // add find
 si3.add(2);
 si3.add(3);
 si3.add(4);

std::cout <<"stampa confronto tra si1 e si3 " << std::endl;
uguali = si3 == si1; 
std::cout << uguali << std::endl;


setInt si4(si3);


std::cout <<"stampa confronto tra si3 e si4 " << std::endl;
uguali = si4 == si3; 
std::cout << uguali << std::endl;


setInt si5;
setInt si6;



std::cout << "stampa confronto tra si5 e si6 " << std::endl;

 uguali = si5 == si6;
 std::cout << uguali <<std::endl;

 std::cout << "stampa confronto tra si3 e si6 " << std::endl;

 uguali = si3 == si6;
 std::cout << uguali <<std::endl;


}

// -----------------------------------------------------------------------------------------

/**
  Test della stampa del set non constante passato come parametro

  @brief Test della stampa del set non constante passato come parametro

  @param si1 set da usare per i test
*/


void test_set_int(setInt &si1){


std::cout<<"******** Test stampa con iteratori set interi ********"<<std::endl;

	si1.add(56);
  si1.add(90);
  si1.add(60);
  si1.add(5);
  
	std::cout<<"Stampa con iteratori"<<std::endl;

	setInt::const_iterator i; 
	setInt::const_iterator ie; 
for(i=si1.begin(),ie=si1.end(); i!=ie; ++i)
	 std::cout<<*i<<std::endl;


std::cout<<"Stampa con iteratori su un set vuoto"<<std::endl;

setInt siV;
for(i=siV.begin(),ie=siV.end(); i!=ie; ++i)
   std::cout<<*i<<std::endl;


}


// ---------------------------------------------------------

/**
  Test di costruzione del set con gli iteratori

  @brief Test di costruzione del set con gli iteratori
*/

void test_costruttore_con_iteratori_interi(){


	std::cout<<"******** Test costruttore con iteratori set interi ********"<<std::endl;


		int a[5] = {20,10,5,100,80};

 		setInt si(a,a+5); // ctor con i due iteratori

  std::cout << "Stampa di si dopo costruttore secondario:" << std::endl;
  std::cout << si << std::endl;  // operator<<


  std::cout<<"test costruttore con iteratori con conversione di tipo da int a float" <<std::endl;

  float v[5]={1.1, 2.5, -1.3, 0.00002, 5.3333};

  setInt si2(v, v+5);

   std::cout << "Stampa di si2 dopo costruttore secondario:" << std::endl;
  std::cout << si2 << std::endl;  // operator<<



}

// ---------------------------------------------------------------------------

/**
  Test della stampa del set constante passato come parametro

  @brief Test della stampa del set constante passato come parametro

  @param si1 set costante da usare per i test
*/



void test_const_set_int(const setInt &si1){

	std::cout<<"******** Test sul set costante di interi ********"<<std::endl;

  // add e remove non sono chiamabili su un oggetto const

  std::cout<<"Stampa con operator<<"<<std::endl;
  std::cout<<si1<<std::endl;

  try{
  std::cout << "stampa i-esimo elemento" << std::endl;
  std::cout << si1[1] << std::endl;

  }catch (const std::out_of_range& ex) {
    std::cerr << "Out of Range error: " << ex.what() << std::endl;
  }

  setInt si2(si1);

  bool vero = si2 == si1;

   std::cout << "confronto si1 e si2" << std::endl;
  std::cout << vero << std::endl;

  std::cout<<"Dimensione del set: "<<si1.getSetSize()<<std::endl;


  std::cout << "test operatore assegnamento su set costanti " << std::endl;

  setInt si3;
  
  si3 = si1;

  std::cout << "stampa si3 " << std::endl;

  std::cout << si3 << std::endl;

  std::cout<<"Stampa con iteratori"<<std::endl;

  setInt::const_iterator i,ie;

  for(i=si1.begin(),ie=si1.end(); i!=ie; ++i)
    std::cout<<*i<<std::endl;


  std::cout << "test filter_out su un set di interi costante" << std::endl;

  is_even ise;

  setInt filt = filter_out(si1, ise);

  std::cout << "stampa di filt " <<std::endl;

  std::cout << filt << std::endl;


  std::cout << "test operatore + su un set di interi di cui uno costante" << std::endl;


  setInt plusSet = si1 + si2 ;

  std::cout << "stampa di plusSet " <<std::endl;

  std::cout << plusSet << std::endl;


  std::cout << "test operatore - su un set di interi di cui uno costante" << std::endl;


  setInt minusSet = si1 - si2 ;

  std::cout << "stampa di minusSet " <<std::endl;

  std::cout << minusSet << std::endl;






}


// ---------------------------------------------------------------------
/**
  Test del operatore + su punti

  @brief Test del operatore + su punti
*/

void test_operator_plus_interi(void){

std::cout<<"******** Test operator + su un set di interi ********"<<std::endl;
	setInt si;
 si.add(0); // add find
 si.add(1);
 si.add(2);
 si.add(3);
 si.add(4);


setInt si2;
 si2.add(4); // add find
 si2.add(5);
 si2.add(6);
 si2.add(7);
 


 setInt si3 = si +si2;

 std::cout<<"stampa risultato operator + su interi" << std::endl;

 std::cout << si3 << std::endl;

 std::cout <<"stampa dimensione si3" << std::endl;

 std::cout << si3.getSetSize() << std::endl;


 std::cout << "test operator + su set vuoti di interi" << std::endl;

 setInt si4;
 setInt si5;

 setInt si6 = si4+si5;

std::cout<<"stampa risultato operator + su set interi vuoti" << std::endl;

 std::cout << si6 << std::endl;

 std::cout <<"stampa dimensione si6" << std::endl;

 std::cout << si6.getSetSize() << std::endl;

std::cout << "test operator + su set di interi uno vuoto e l'altro no" << std::endl;

setInt si7 = si2 + si5;

std::cout<<"stampa risultato operator + su set interi uno vuoto e l'altro no" << std::endl;

 std::cout << si7 << std::endl;


std::cout <<"stampa dimensione si7" << std::endl;

 std::cout << si7.getSetSize() << std::endl;




}
//--------------------------------------------------------------------


/**
  Test della funzione filter_out su interi

  @brief Test della funzione filter_out su interi
*/

void test_filter_out_interi(void){

std::cout<<"******** Test metodo globale filter_out su un set di interi ********"<<std::endl;

	setInt si;
 si.add(1); // add find
 si.add(2);
 si.add(3);
 si.add(4);
	
	is_even ie;

 setInt si2 = filter_out(si, ie);

 std::cout<<"stampa risultato filter_out " << std::endl;

 std::cout << si2 << std::endl;


 std::cout << "test filter_out su un set vuoto " <<std::endl;

 setInt si3;

 setInt si4 = filter_out(si3, ie);

 std::cout << "stampa di si4 " << std::endl;

 std::cout << si4 << std::endl;

}



//--------------------------------------------------------------------

/**
  Test del operatore - su interi

  @brief Test del operatore - su interi
*/


void test_operator_minus_interi(void){

std::cout<<"******** Test operator - su un set di interi ********"<<std::endl;
	setInt si;
 si.add(0); // add find
 si.add(1);
 si.add(2);
 si.add(3);
 si.add(4);


setInt si2;
 si2.add(5); // add find
 si2.add(6);
 si2.add(0);
 si2.add(8);
 si2.add(4);
 si2.add(2);
 


 setInt si3 = si - si2;

 std::cout<<"stampa risultato operator - su interi" << std::endl;

 std::cout << si3 << std::endl;


 std::cout << "test operatore - su un set vuoti " <<std::endl;

 setInt si4;

 setInt si5;

 setInt si6 = si4 - si5;

 std::cout << "stampa di si6 " << std::endl;

 std::cout << si6 << std::endl;


 std::cout << "test operatore - su un set vuoto e uno no " <<std::endl;

 setInt si7 = si4 - si2;

 std::cout << "stampa di si7 " << std::endl;

 std::cout << si7 << std::endl;

}


/**
  Test del operatore + e - su set di interi costanti

  @brief Test del operatore + e - su set di interi costanti

  @param si1 set costante d'interi usato per i test

  @param si2 set costante d'interi usato per i test
*/

void test_operatoriGlobali_const_set_int(const setInt &si1, const setInt &si2){

std::cout << "******* Test operatori globali su set di interi constanti ******" <<std::endl;


std::cout << "operatore + " <<std::endl;

setInt plus = si1 + si2;

std::cout << "stampa di plus " << std::endl;

std::cout << plus << std::endl;

std::cout << "operatore - " <<std::endl;

setInt diff = si1 - si2;

std::cout << "stampa di diff " << std::endl;

std::cout << diff << std::endl;



  }



/*   ****** fine test su interi ****** */



/* ******** inizio test tipo dato custom point ********* */


/**
  Test metodi fondamentali struct point

  @brief Test metodi fondamentali struct point
*/

void test_metodi_fondamentali_punti(){

  std::cout<<"******** Test metodi fondamentali su punti ********"<<std::endl;

  std::cout << "costruttore di default " << std::endl;

  point p;

  std::cout << "stampa di p " << std::endl;

  std::cout << p << std::endl;


  std::cout << "costruttore secondario " << std::endl;

  point p1(2,2);

  std::cout << "stampa di p1 " << std::endl;

  std::cout << p1 << std::endl;


  std::cout << "copy constructor" << std::endl;

  point p2(p1);

  std::cout << "stampa di p2 " << std::endl;

  std::cout << p2 << std::endl;


  std::cout << "operatore assegnamento " << std::endl;

  point p3 = p1;

  std::cout << "stampa di p3 " << std::endl;

  std::cout << p3 << std::endl;


   std::cout << "test auto assegnamento " << std::endl;

   p3 = p3;

  std::cout << "stampa di p3 " << std::endl;

  std::cout << p3 << std::endl;



}




/**
  Test dei metodi fondamentali su set di punti

  @brief Test dei metodi fondamentali su set di punti
*/
void test_metodi_punti() {
  std::cout<<"******** Test metodi fondamentali su un set di punti ********"<<std::endl;

   setPoint sp;  // ctor default


  std::cout << "size del set creato dal costruttore di default : " << sp.getSetSize() << std::endl;

   std::cout << "stampa del set creato con il costruttore di default" << std::endl;
   std::cout << sp << std::endl;

   sp.add(point(0,0)); // add find
   sp.add(point(1,1));
   sp.add(point(2,2));
   sp.add(point(3,3));

  // controllo su inserimento elementi duplicati
   sp.add(point(0,0)); 
   sp.add(point(2,2));

   std::cout << "Stampa di sp dopo inserimenti:" << std::endl;
   std::cout << sp << std::endl;  // operator<<

   std::cout << "Stampa la dimensione di sp " << std::endl;

   std::cout << sp.getSetSize() << std::endl;


   std::cout<< "copy constructor su set vuoto" << std::endl;
   setPoint sp4;

   setPoint sp5(sp4);

   std::cout << "stampa del contenuto del set sp5" << std::endl;
   std::cout << sp5 << std::endl;

   std::cout << "stampa size sp5" << std::endl;
   std::cout << sp5.getSetSize() << std::endl;

   std::cout<< "operatore assegnamento su set vuoti" << std::endl;

   setPoint sp6;

   sp6=sp5;

   std::cout << "stampa del contenuto del set sp6" << std::endl;
   std::cout << sp6 << std::endl;

   std::cout << "stampa size sp6" << std::endl;
   std::cout << sp6.getSetSize() << std::endl;



   setPoint sp2(sp);  // cctor

   std::cout << "Stampa di sp2 dopo copy constructor:" << std::endl;
   std::cout << sp2 << std::endl;  // operator<<

   setPoint sp3;

   sp3 = sp; // operator=

   std::cout << "Stampa di sp3 dopo assegnamento:" << std::endl;
   std::cout << sp3 << std::endl;  // operator<<


   std::cout << "Stampa di sp dopo rimozione elemento di testa" << std::endl;
   	
   sp.remove(point(0,0));

   std::cout << sp << std::endl;


	std::cout << "Stampa di sp dopo rimozione elemento di coda" << std::endl;
   	
	sp.remove(point(3,3));

  std::cout << sp << std::endl;


	std::cout << "Stampa di sp dopo rimozione elemento in centro" << std::endl;
   	
	sp.remove(point(2,2));

  std::cout << sp << std::endl;


  std::cout << "Stampa di sp dopo rimozione elemento non presente" << std::endl;
   	
	sp.remove(point(20,20));

  std::cout << sp << std::endl;


  std::cout << "Stampa la dimensione di sp dopo rimozioni " << std::endl;

  std::cout << sp.getSetSize() << std::endl;



  std::cout<<"******** Test altri metodi su un set di punti ********"<<std::endl;

	bool uguali = false;

	setPoint spA;  // ctor default

  spA.add(point(6,6)); // add find
  spA.add(point(7,7));
  spA.add(point(8,8));
  spA.add(point(9,9));

try{
  std::cout <<"stampa il valore del primo elemento " << std::endl;
  std::cout << spA[0] << std::endl;


  std::cout <<"stampa il valore del secondo elemento " << std::endl;
  std::cout << spA[1] << std::endl;


  std::cout <<"stampa il valore dell'ultimo elemento " << std::endl;
  std::cout << spA[3] << std::endl;

  std::cout <<"controllo eccezione con indice negativo " << std::endl;
  std::cout << spA[-5] << std::endl;

 
  std::cout <<"stampa il valore di un elemento non presente " << std::endl;
  std::cout << spA[20] << std::endl;

std::cout <<"stampa il valore di un elemento di un set vuoto " << std::endl;
setPoint sv;
std::cout << sv[20] << std::endl;

}catch (const std::out_of_range& ex) {
    std::cerr << "Out of Range error: " << ex.what() << std::endl;
  }


  setPoint spB;  // ctor default

  spB.add(point(5,5)); 
  spB.add(point(8,8));


 std::cout <<"stampa confronto tra spA e spB " << std::endl;
 uguali = spA == spB; 
 std::cout << uguali << std::endl;



setPoint spC;
 spC.add(point(1,1)); // add find
 spC.add(point(2,2));
 spC.add(point(3,3));
 spC.add(point(4,4));

std::cout <<"stampa confronto tra spA e spC " << std::endl;
uguali = spC == spA; 
std::cout << uguali << std::endl;


setPoint spD(spC);


std::cout <<"stampa confronto tra spC e spD " << std::endl;
uguali = spC == spD; 
std::cout << uguali << std::endl;


std::cout << "stampa confronto tra sp5 e sp6 " << std::endl;

 uguali = sp5 == sp6;
 std::cout << uguali <<std::endl;

 std::cout << "stampa confronto tra spB e sp6 " << std::endl;

 uguali = spB == sp6;
 std::cout << uguali <<std::endl;



std::cout<<"******** Test costruttore con iteratori set punti ********"<<std::endl;


		point a[5] = {point(1,2), point(3,2), point(2,2), point(0,2), point(8,2)};

 		setPoint spIt(a,a+5); // ctor con i due iteratori

  std::cout << "Stampa di spIt dopo costruttore secondario:" << std::endl;
  std::cout << spIt << std::endl;  // operator<<





std::cout<<"********** Stampa con iteratori su set di punti ***********"<<std::endl;

	setPoint::const_iterator i; 
	setPoint::const_iterator ie; 
for(i=spC.begin(),ie=spC.end(); i!=ie; ++i)
	 std::cout<<*i<<std::endl;

std::cout<<"********** Stampa con iteratori su set di punti vuoto ***********"<<std::endl;

setPoint spV;
for(i=spV.begin(),ie=spV.end(); i!=ie; ++i)
   std::cout<<*i<<std::endl;


} 

// -------------------------------------------------------


/**
  Test della funzione filter_out su punti

  @brief Test della funzione filter_out su punti
*/

void test_filter_out_point(void){

	std::cout<<"******** Test metodo globale filter_out su un set di punti ********"<<std::endl;

	setPoint sp;
 sp.add(point(0,0)); // add find
 sp.add(point(1,1));
 sp.add(point(2,2));
 sp.add(point(3,3));
	sp.add(point(4,4));
	
	
	is_even_point iep;

 setPoint sp2 = filter_out(sp, iep);

 std::cout<<"stampa risultato filter_out " << std::endl;

 std::cout << sp2 << std::endl;

 std::cout << "test filter_out su un set vuoto" << std::endl;

 setPoint sp1;

 setPoint sp3 = filter_out(sp1, iep);

 std::cout<< "stampa set sp3" << std::endl;

 std::cout << sp3 <<std::endl;



}

//-----------------------------------------------------

/**
  Test del operatore + su punti

  @brief Test del operatore + su punti
*/


void test_operator_plus_point(void){

std::cout<<"******** Test operator + su un set di punti ********"<<std::endl;
	setPoint sp;
 sp.add(point(0,0)); // add find
 sp.add(point(1,1));
 sp.add(point(2,2));
 sp.add(point(3,3));
 sp.add(point(4,4));


setPoint sp2;
 sp2.add(point(1,0)); // add find
 sp2.add(point(1,2));
 sp2.add(point(3,2));
 sp2.add(point(5,3));
 sp2.add(point(2,2));
 


 setPoint sp3 = sp + sp2;

 std::cout<<"stampa risultato operator + su punti" << std::endl;

 std::cout << sp3 << std::endl;


 std::cout << "test operatore + tra set di punti vuoti " << std::endl;

 setPoint sp4;
 setPoint sp5;

 setPoint sp6 = sp4 + sp5;

 std::cout << "stampa sp6 " << std::endl;

 std::cout << sp6 << std::endl;

 std::cout << "test operatore + su un set di punti vuoto e l'altro no" << std::endl;

 setPoint sp7 = sp4 +sp;

 std::cout << "stampa sp7 " << std::endl;

 std::cout << sp7 << std::endl;



}


// ----------------------------------------------------

/**
  Test del operatore - su punti

  @brief Test del operatore - su punti
*/

void test_operator_minus_point(void){

std::cout<<"******** Test operator - su un set di punti ********"<<std::endl;
	setPoint sp;
 sp.add(point(0,0)); // add find
 sp.add(point(1,1));
 sp.add(point(2,2));
 sp.add(point(3,3));
 sp.add(point(4,4));


setPoint sp2;
 sp2.add(point(5,0)); // add find
 sp2.add(point(1,1));
 sp2.add(point(3,2));
 sp2.add(point(0,4));
 sp2.add(point(3,3));
 
 


 setPoint sp3 = sp - sp2;

 std::cout<<"stampa risultato operator - su punti" << std::endl;

 std::cout << sp3 << std::endl;


std::cout << "test operatore - tra set di punti vuoti " << std::endl;

 setPoint sp4;
 setPoint sp5;

 setPoint sp6 = sp4 - sp5;

 std::cout << "stampa sp6 " << std::endl;

 std::cout << sp6 << std::endl;

 std::cout << "test operatore - su un set di punti vuoto e l'altro no" << std::endl;

 setPoint sp7 = sp4 - sp;

 std::cout << "stampa sp7 " << std::endl;

 std::cout << sp7 << std::endl;


}



/**
  Test del operatore + e - e filter_out su set di punti costanti

  @brief Test del operatore + e - e filter_out su set di punti costanti

  @param sp1 set costante di punti usato per i test

  @param sp2 set costante di punti usato per i test



*/

void test_operatoriGlobali_const_set_point(const setPoint &sp1, const setPoint &sp2){

std::cout << "******* Test operatori globali su set di punti constanti ******" <<std::endl;


std::cout << "operatore + " <<std::endl;

setPoint plus = sp1 + sp2;

std::cout << "stampa di plus " << std::endl;

std::cout << plus << std::endl;

std::cout << "operatore - " <<std::endl;

setPoint diff = sp1 - sp2;

std::cout << "stampa di diff " << std::endl;

std::cout << diff << std::endl;

is_even_point ie;

std::cout << "funzione filter_out " <<std::endl;

setPoint filt = filter_out(sp1, ie);

std::cout << "stampa di filt " << std::endl;

std::cout << filt << std::endl;


  }



/* ******** fine test tipo dato custom point ********* */



/* ******** inizio test tipo dato custom person ********* */



  /**
  Test metodi fondamentali struct person

  @brief Test metodi fondamentali struct person
*/

void test_metodi_fondamentali_persone(){

  std::cout<<"******** Test metodi fondamentali su persone ********"<<std::endl;

  std::cout << "costruttore di default " << std::endl;

  person p;

  std::cout << "stampa di p " << std::endl;

  std::cout << p << std::endl;


  std::cout << "costruttore secondario " << std::endl;

  person p1("ale", "catta", 22);

  std::cout << "stampa di p1 " << std::endl;

  std::cout << p1 << std::endl;


  std::cout << "copy constructor" << std::endl;

  person p2(p1);

  std::cout << "stampa di p2 " << std::endl;

  std::cout << p2 << std::endl;


  std::cout << "operatore assegnamento " << std::endl;

  person p3 = p1;

  std::cout << "stampa di p3 " << std::endl;

  std::cout << p3 << std::endl;


  std::cout << "test auto assegnamento " << std::endl;

   p3 = p3;

  std::cout << "stampa di p3 " << std::endl;

  std::cout << p3 << std::endl;





}






/**
  Test dei metodi fondamentali su set di persone

  @brief Test dei metodi fondamentali su set di persone
*/
void test_metodi_person() {
  std::cout<<"******** Test metodi fondamentali su un set di persone ********"<<std::endl;

   setPerson sp;  // ctor default

   std::cout << "size del set creato dal costruttore di default : " << sp.getSetSize() << std::endl;

   std::cout << "stampa del set creato con il costruttore di default" << std::endl;
   std::cout << sp << std::endl;

   sp.add(person("ale", "catta", 22)); // add find
   sp.add(person("ale", "catta", 25));
   sp.add(person("mario", "rossi", 40));
   sp.add(person("piero", "angela", 85));

  // controllo su inserimento elementi duplicati
  sp.add(person("ale", "catta", 22));  
  sp.add(person("mario", "rossi", 40));

   std::cout << "Stampa di sp dopo inserimenti:" << std::endl;
   std::cout << sp << std::endl;  // operator<<

   std::cout << "Stampa la dimensione di sp " << std::endl;

   std::cout << sp.getSetSize() << std::endl;



   setPerson sp2(sp);  // cctor

   std::cout << "Stampa di sp2 dopo copy constructor:" << std::endl;
   std::cout << sp2 << std::endl;  // operator<<

   
   std::cout<< "copy constructor su set vuoto" << std::endl;
   setPerson sp4;

   setPerson sp5(sp4);

   std::cout << "stampa del contenuto del set sp5" << std::endl;
   std::cout << sp5 << std::endl;

   std::cout << "stampa size sp5" << std::endl;
   std::cout << sp5.getSetSize() << std::endl;

   std::cout<< "operatore assegnamento su set vuoti" << std::endl;

   setPerson sp6;

   sp6=sp5;

   std::cout << "stampa del contenuto del set sp6" << std::endl;
   std::cout << sp6 << std::endl;

   std::cout << "stampa size sp6" << std::endl;
   std::cout << sp6.getSetSize() << std::endl;



   setPerson sp3;

   sp3 = sp; // operator=

   std::cout << "Stampa di sp3 dopo assegnamento:" << std::endl;
   std::cout << sp3 << std::endl;  // operator<<


   std::cout << "Stampa di sp dopo rimozione elemento di testa" << std::endl;
    
   sp.remove(person("ale", "catta", 22));

   std::cout << sp << std::endl;


  std::cout << "Stampa di sp dopo rimozione elemento di coda" << std::endl;
    
  sp.remove(person("piero", "angela", 85));

  std::cout << sp << std::endl;


  std::cout << "Stampa di sp dopo rimozione elemento in centro" << std::endl;
    
  sp.remove(person("mario", "rossi", 40));

  std::cout << sp << std::endl;


  std::cout << "Stampa di sp dopo rimozione elemento non presente" << std::endl;
    
  sp.remove(person("pippo", "paperino", 40));

  std::cout << sp << std::endl;


  std::cout << "Stampa la dimensione di sp dopo rimozioni " << std::endl;

  std::cout << sp.getSetSize() << std::endl;



  std::cout<<"******** Test altri metodi su un set di persone ********"<<std::endl;

  bool uguali = false;

  setPerson spA;  // ctor default

  spA.add(person("luca", "lolli", 15)); // add find
  spA.add(person("lucia", "lolli", 20));
  spA.add(person("marika", "luppi", 45));
  spA.add(person("fabio", "lolli", 50));

 try{

  std::cout <<"stampa il valore del primo elemento " << std::endl;
  std::cout << spA[0] << std::endl;


  std::cout <<"stampa il valore del secondo elemento " << std::endl;
  std::cout << spA[1] << std::endl;


  std::cout <<"stampa il valore dell'ultimo elemento " << std::endl;
  std::cout << spA[3] << std::endl;

  std::cout <<"controllo eccezione con indice negativo " << std::endl;
  std::cout << spA[-5] << std::endl;

 
  std::cout <<"stampa il valore di un elemento non presente " << std::endl;
  std::cout << spA[20] << std::endl;

  std::cout <<"stampa il valore di un elemento di un set vuoto " << std::endl;
  setPerson sv;
  std::cout << sv[20] << std::endl;


}catch (const std::out_of_range& ex) {
    std::cerr << "Out of Range error: " << ex.what() << std::endl;
  }

  setPerson spB;  // ctor default

  spB.add(person("fabio", "deluigi", 50)); 
  spB.add(person("fabrizio", "sironi", 40));


 std::cout <<"stampa confronto tra spA e spB " << std::endl;
 uguali = spA == spB; 
 std::cout << uguali << std::endl;


 std::cout << "stampa confronto tra sp5 e sp6 " << std::endl;

 uguali = sp5 == sp6;
 std::cout << uguali <<std::endl;

 std::cout << "stampa confronto tra spB e sp6 " << std::endl;

 uguali = spB == sp6;
 std::cout << uguali <<std::endl;



setPerson spC;
 spC.add(person("roberto", "del prete", 50)); // add find
 spC.add(person("lucia", "brambilla", 80));
 spC.add(person("massimo", "bottura", 33));
 spC.add(person("giorgia", "favino", 20));

std::cout <<"stampa confronto tra spA e spC " << std::endl;
uguali = spC == spA; 
std::cout << uguali << std::endl;


setPerson spD(spC);


std::cout <<"stampa confronto tra spC e spD " << std::endl;
uguali = spC == spD; 
std::cout << uguali << std::endl;



std::cout<<"******** Test costruttore con iteratori set persone ********"<<std::endl;


    person a[5] = {person("roberto", "del prete", 50), person("lucia", "brambilla", 80), person("massimo", "bottura", 33),
     person("giorgia", "favino", 20), person("giorgia", "favino", 20)};

    setPerson spIt(a,a+5); // ctor con i due iteratori

  std::cout << "Stampa di spIt dopo costruttore secondario:" << std::endl;
  std::cout << spIt << std::endl;  // operator<<





std::cout<<"********** Stampa con iteratori su set di persone ***********"<<std::endl;

  setPerson::const_iterator i; 
  setPerson::const_iterator ie; 
for(i=spC.begin(),ie=spC.end(); i!=ie; ++i)
   std::cout<<*i<<std::endl;



std::cout<<"********** Stampa con iteratori su set di persone vuoto ***********"<<std::endl;

setPerson spV;
for(i=spV.begin(),ie=spV.end(); i!=ie; ++i)
   std::cout<<*i<<std::endl;


} 



//-----------------------------------------------------------------------------------


/**
  Test della funzione filter_out su persone

  @brief Test della funzione filter_out su persone
*/

void test_filter_out_person(void){

  std::cout<<"******** Test metodo globale filter_out su un set di persone ********"<<std::endl;

  setPerson sp;

  sp.add(person("luca", "lolli", 15)); // add find
  sp.add(person("lucia", "lolli", 20));
  sp.add(person("marika", "luppi", 45));
  sp.add(person("fabio", "lolli", 50));
  sp.add(person("orietta", "berti", 2));
 
 
  
  
  is_adult ia;

 setPerson sp2 = filter_out(sp, ia);

 std::cout<<"stampa risultato filter_out " << std::endl;

 std::cout << sp2 << std::endl;

 std::cout << "test filter_out su un set vuoto" << std::endl;

 setPerson sp1;

 setPerson sp3 = filter_out(sp1, ia);

 std::cout<< "stampa set sp3" << std::endl;

 std::cout << sp3 <<std::endl;





}


//------------------------------------------------------------------------------

/**
  Test del operatore + su persone

  @brief Test del operatore + su persone
*/


void test_operator_plus_person(void){

std::cout<<"******** Test operator + su un set di persone ********"<<std::endl;
  
   setPerson sp;

  sp.add(person("luca", "lolli", 15)); // add find
  sp.add(person("lucia", "lolli", 20));
  sp.add(person("marika", "luppi", 45));
  sp.add(person("fabio", "lolli", 50));
  sp.add(person("orietta", "berti", 2));


setPerson sp2;
 sp2.add(person("roberto", "del prete", 50)); // add find
 sp2.add(person("lucia", "brambilla", 80));
 sp2.add(person("massimo", "bottura", 33));
 sp2.add(person("giorgia", "favino", 20));
 sp2.add(person("fabio", "lolli", 50));
 sp2.add(person("orietta", "berti", 2));


 setPerson sp3 = sp + sp2;

 std::cout<<"stampa risultato operator + su persone" << std::endl;

 std::cout << sp3 << std::endl;


 std::cout << "test operatore + tra set di persone vuoti " << std::endl;

 setPerson sp4;
 setPerson sp5;

 setPerson sp6 = sp4 + sp5;

 std::cout << "stampa sp6 " << std::endl;

 std::cout << sp6 << std::endl;

 std::cout << "test operatore + su un set di persone vuoto e l'altro no" << std::endl;

 setPerson sp7 = sp4 + sp;

 std::cout << "stampa sp7 " << std::endl;

 std::cout << sp7 << std::endl;



}


//---------------------------------------------------------------------------------------------



  /**
  Test del operatore - su persone

  @brief Test del operatore - su persone
*/

void test_operator_minus_person(void){

std::cout<<"******** Test operator - su un set di persone ********"<<std::endl;
   setPerson sp;

  sp.add(person("luca", "lolli", 15)); // add find
  sp.add(person("lucia", "lolli", 20));
  sp.add(person("marika", "luppi", 45));
  sp.add(person("fabio", "lolli", 50));
  sp.add(person("orietta", "berti", 2));


setPerson sp2;
 sp2.add(person("roberto", "del prete", 50)); // add find
 sp2.add(person("lucia", "brambilla", 80));
 sp2.add(person("massimo", "bottura", 33));
 sp2.add(person("giorgia", "favino", 20));
 sp2.add(person("fabio", "lolli", 50));
 sp2.add(person("orietta", "berti", 2));


 setPerson sp3 = sp - sp2;
 
 std::cout<<"stampa risultato operator - su persone" << std::endl;

 std::cout << sp3 << std::endl;


std::cout << "test operatore - tra set di persone vuoti " << std::endl;

 setPerson sp4;
 setPerson sp5;

 setPerson sp6 = sp4 - sp5;

 std::cout << "stampa sp6 " << std::endl;

 std::cout << sp6 << std::endl;

 std::cout << "test operatore - su un set di persone vuoto e l'altro no" << std::endl;

 setPerson sp7 = sp4 - sp;

 std::cout << "stampa sp7 " << std::endl;

 std::cout << sp7 << std::endl;


}





/**
  Test del operatore + e - e filter_out su set di persone costanti

  @brief Test del operatore + e - e filter_out su set di persone costanti

  @param sp1 set costante di persone usato per i test

  @param sp2 set costante di persone usato per i test


*/

void test_operatoriGlobali_const_set_person(const setPerson &sp1, const setPerson &sp2){

std::cout << "******* Test operatori globali su set di persone constanti ******" <<std::endl;


std::cout << "operatore + " <<std::endl;

setPerson plus = sp1 + sp2;

std::cout << "stampa di plus " << std::endl;

std::cout << plus << std::endl;

std::cout << "operatore - " <<std::endl;

setPerson diff = sp1 - sp2;

std::cout << "stampa di diff " << std::endl;

std::cout << diff << std::endl;

is_adult ia;

std::cout << "funzione filter_out " <<std::endl;

setPerson filt = filter_out(sp1, ia);

std::cout << "stampa di filt " << std::endl;

std::cout << filt << std::endl;


  }




/* ******** fine test tipo dato custom person ********* */


/* *************** inizio test funzione emptySet su tipi custom e interi ****************** */

/**
  Test della funzione emptyset su persone, punti e interi

  @brief  Test della funzione emptyset su persone, punti e interi

*/


void empty_set_test(void){


std::cout << "********* test emptySet() su interi ************"  << std::endl;


  setInt si;
 si.add(0); // add find
 si.add(1);
 si.add(2);
 si.add(3);
 si.add(4);


std::cout << "stampa si prima dell'emptySet() " << std::endl;
std::cout << si << std::endl;

si.emptySet();


std::cout << "stampa si dopo dell'emptySet() " << std::endl;
std::cout << si << std::endl;



std::cout << "********* test emptySet() su punti ************"  << std::endl;


setPoint spt;
 spt.add(point(0,0)); // add find
 spt.add(point(1,1));
 spt.add(point(2,2));
 spt.add(point(3,3));
 spt.add(point(4,4));

std::cout << "stampa spt prima dell'emptySet() " << std::endl;
std::cout << spt << std::endl;

spt.emptySet();


std::cout << "stampa spt dopo dell'emptySet() " << std::endl;
std::cout << spt << std::endl;


std::cout << "********* test emptySet() su persone ***********"  << std::endl;

setPerson sp;

sp.add(person("luca", "lolli", 15)); // add find
sp.add(person("lucia", "lolli", 20));
sp.add(person("marika", "luppi", 45));
sp.add(person("fabio", "lolli", 50));
sp.add(person("orietta", "berti", 2));


std::cout << "stampa sp prima dell'emptySet() " << std::endl;
std::cout << sp << std::endl;

sp.emptySet();


std::cout << "stampa sp dopo dell'emptySet() " << std::endl;
std::cout << sp << std::endl;

std::cout << "test emptySet() su set vuoto su di interi" << std::endl;

setInt si2;

si2.emptySet();

std::cout << "dimensione si2 dopo emptySet() " <<std::endl;
std::cout << si2.getSetSize()<<std::endl;

std::cout<< "stampa si2 dopo emptySet() " << std::endl;
std::cout << si2 << std::endl;


std::cout << "test emptySet() su set vuoto su di punti" << std::endl;

setPoint sp2;

sp2.emptySet();

std::cout << "dimensione sp2 dopo emptySet() " <<std::endl;
std::cout << sp2.getSetSize()<<std::endl;

std::cout<< "stampa sp2 dopo emptySet() " << std::endl;
std::cout << sp2 << std::endl;



std::cout << "test emptySet() su set vuoto su persone" << std::endl;

setPerson sper2;

sper2.emptySet();

std::cout << "dimensione sper2 dopo emptySet() " <<std::endl;
std::cout << sper2.getSetSize()<<std::endl;

std::cout<< "stampa sper2 dopo emptySet() " << std::endl;
std::cout << sper2 << std::endl;


}



/* *************** fine test funzione emptySet su tipi custom e interi ****************** */


/* *************** inizio test iteratori interi ****************** */


/**
  Test di iteratori su interi

  @brief  Test di iteratori su interi

*/

void iterator_test_interi(void){


std::cout << "********* test iteratori su interi ************"  << std::endl;

  setInt si;
 si.add(0); // add find
 si.add(1);
 si.add(2);
 si.add(3);
 si.add(4);

std::cout << "test costruttore di default su interi" << std::endl;
  setInt::const_iterator i; 
  setInt::const_iterator ie; 


  std::cout << "test begin iteratore su interi" << std::endl;
  i=si.begin();

  std::cout << "stampa valore di i con operatore *" << std::endl;
  std::cout << *i << std::endl;

  std::cout << "test pre incremento iteratore su interi" << std::endl;
  ++i;
  std::cout << *i << std::endl;


  std::cout << "test post incremento iteratore su interi" << std::endl;
  setInt::const_iterator app;
  app = i++;
  std::cout << *app << std::endl;


  std::cout << "test uguaglinza iteratore su interi" << std::endl;

  std::cout << (i==ie) << std::endl;

   
  std::cout << "test disuguaglinaza iteratori su interi" << std::endl;

  std::cout << (i!=ie) << std::endl;


  std::cout <<"test assegnamento iteratori su interi" << std::endl;

  setInt::const_iterator i2; 

  i2=i;

  std::cout << *i2 << std::endl;

   std::cout << "secondo test uguaglinza iteratore su interi" << std::endl;

   std::cout << (i==i2) << std::endl;


  std::cout <<"test copy constructor iteratori su interi" << std::endl;

  setInt::const_iterator i3(i2); 

  std::cout << *i3 << std::endl;



   

}


/* *************** fine test iteratori interi ****************** */




/* *************** inizio test iteratori su punti ****************** */

/**
  Test di iteratori su punti

  @brief  Test di iteratori su punti

*/


void iterator_test_punti(void){



  std::cout << "********* test iteratori su punti ************"  << std::endl;

  setPoint spt;
 spt.add(point(0,0)); // add find
 spt.add(point(1,1));
 spt.add(point(2,2));
 spt.add(point(3,3));
 spt.add(point(4,4));

std::cout << "test costruttore di default su punti" << std::endl;
  setPoint::const_iterator i; 
  setPoint::const_iterator ie; 


  std::cout << "test begin iteratore su punti" << std::endl;
  i=spt.begin();

  std::cout << "stampa valore di i con operatore *" << std::endl;
  std::cout << *i << std::endl;

  std::cout << "stmapa valore di i con operatore -> " << std::endl;
  std::cout << "(" <<i -> x << ","<< i->y << ")" <<std::endl;
  

  std::cout << "test pre incremento iteratore su punti" << std::endl;
  ++i;
  std::cout << *i << std::endl;


  std::cout << "test post incremento iteratore su punti" << std::endl;
  setPoint::const_iterator app;
  app = i++;
  std::cout << *app << std::endl;


  std::cout << "test uguaglinza iteratore su punti" << std::endl;

  std::cout << (i==ie) << std::endl;

   
  std::cout << "test disuguaglinaza iteratori su punti" << std::endl;

  std::cout << (i!=ie) << std::endl;


  std::cout <<"test assegnamento iteratori su punti" << std::endl;

  setPoint::const_iterator i2; 

  i2=i;

  std::cout << *i2 << std::endl;

   std::cout << "secondo test uguaglinza iteratore su punti" << std::endl;

   std::cout << (i==i2) << std::endl;


  std::cout <<"test copy constructor iteratori su punti" << std::endl;

  setPoint::const_iterator i3(i2); 

  std::cout << *i3 << std::endl;




  
}


/* *************** fine test iteratori su punti ****************** */



/* *************** inizio test iteratori su persone ****************** */

/**
  Test di iteratori su persone

  @brief  Test di iteratori su persone

*/


void iterator_test_persone(void){


  std::cout << "********* test iteratori su persone ************"  << std::endl;

  setPerson sp;

sp.add(person("luca", "lolli", 15)); // add find
sp.add(person("lucia", "lolli", 20));
sp.add(person("marika", "luppi", 45));
sp.add(person("fabio", "lolli", 50));
sp.add(person("orietta", "berti", 2));

std::cout << "test costruttore di default su persone" << std::endl;
  setPerson::const_iterator i; 
  setPerson::const_iterator ie; 


  std::cout << "test begin iteratore su persone" << std::endl;
  i=sp.begin();

  std::cout << "stampa valore di i con operatore *" << std::endl;
  std::cout << *i << std::endl;

  std::cout << "stmapa valore di i con operatore -> " << std::endl;
  std::cout << "(" <<i -> name << ",";
  std::cout << i -> surname << ",";
  std::cout << i -> age << ")"<<std::endl;

  std::cout << "test pre incremento iteratore su persone" << std::endl;
  ++i;
  std::cout << *i << std::endl;


  std::cout << "test post incremento iteratore su persone" << std::endl;
  setPerson::const_iterator app;
  app = i++;
  std::cout << *app << std::endl;


  std::cout << "test uguaglinza iteratore su persone" << std::endl;

  std::cout << (i==ie) << std::endl;

   
  std::cout << "test disuguaglinaza iteratori su persone" << std::endl;

  std::cout << (i!=ie) << std::endl;


  std::cout <<"test assegnamento iteratori su persone" << std::endl;

  setPerson::const_iterator i2; 

  i2=i;

  std::cout << *i2 << std::endl;

   std::cout << "secondo test uguaglinza iteratore su persone" << std::endl;

   std::cout << (i==i2) << std::endl;


  std::cout <<"test copy constructor iteratori su persone" << std::endl;

  setPerson::const_iterator i3(i2); 

  std::cout << *i3 << std::endl;


}







/* *************** fine test iteratori su persone ****************** */



/* ************ test funtori di uguaglianza ***************** */

/**
  Test dei predicati per interi punti e persone

  @brief Test dei predicati per interi punti e persone
*/


void test_predicati(void){

 std::cout << "******* Test predicato uguaglianza su interi ******" <<std::endl;

  int_equal eql;

  std::cout << "predicato uguaglianza tra interi uguali " << std::endl;

  std::cout << (eql(1,1)) << std::endl;

  std::cout << "predicato uguaglianza tra interi diversi " << std::endl;

  std::cout << (eql(2,1)) << std::endl;


std::cout << "******* Test predicato is_even su interi ******"<<std::endl;

is_even ev;

std::cout << "predicato is_even su un intero pari " << std::endl;

std::cout << ((ev(2))) << std::endl;

std::cout << "predicato is_even su un intero dispari " << std::endl;

std::cout << ((ev(3))) << std::endl;


  



std::cout << "******* Test predicato uguaglianza su punti ******" <<std::endl;

equal_point eqp;

std::cout << "predicato uguaglianza tra punti uguali " << std::endl;

std::cout << (eqp(point(1,1), point(1,1))) << std::endl;

std::cout << "predicato uguaglianza tra punti diversi " << std::endl;

std::cout << (eqp(point(1,1), point(2,1))) << std::endl;


std::cout << "******* Test predicato is_even_point su punti ******" <<std::endl;

is_even_point evp;

std::cout << "predicato is_even su un punto pari " << std::endl;

std::cout << (evp(point(2,2))) << std::endl;

std::cout << "predicato is_even su un punto dispari " << std::endl;

std::cout << (evp(point(3,2))) << std::endl;



std::cout << "******* Test predicato uguaglianza su persone ******" <<std::endl;

equal_person eqpers;


std::cout << "predicato uguaglianza tra persone uguali " << std::endl;

std::cout << (eqpers(person("ale","catta",22), person("ale", "catta", 22))) << std::endl;

std::cout << "predicato uguaglianza tra persone diverse " << std::endl;

std::cout << (eqpers(person("ale","catta",22),person("paolo", "rossi", 22))) << std::endl;


std::cout << "******* Test predicato is_adult su persone ******" <<std::endl;

is_adult ia;

std::cout << "predicato is_adult su una persona adulta " << std::endl;

std::cout << (ia(person("ale","catta",22))) << std::endl;

std::cout << "predicato is_adult su una persona non adulta " << std::endl;

std::cout << (ia(person("ale","catta",5))) << std::endl;


}



/* ************ fine test funtori di uguaglianza ***************** */





int main(int argc, char const *argv[])
{
	
test_metodi_fondamentali_interi();

test_altri_metodi_su_interi();

test_costruttore_con_iteratori_interi();

setInt si1;

test_set_int(si1);

test_const_set_int(si1);

test_filter_out_interi();

test_operator_plus_interi();

test_operator_minus_interi();

setInt si2;

si2.add(2);
si2.add(5);
si2.add(7);

test_operatoriGlobali_const_set_int(si1, si2);

test_metodi_fondamentali_punti();

test_metodi_punti();

test_filter_out_point();

test_operator_plus_point();

test_operator_minus_point();

setPoint sp1;

sp1.add(point(5,4));
sp1.add(point(2,2));
sp1.add(point(3,2));


setPoint sp2;

sp2.add(point(2,2));
sp2.add(point(4,2));
sp2.add(point(5,4));

test_operatoriGlobali_const_set_point(sp1, sp2);

test_metodi_fondamentali_persone();

test_metodi_person();

test_filter_out_person();

test_operator_plus_person();

test_operator_minus_person();


setPerson spe1;

spe1.add(person("ale", "catta", 15));
spe1.add(person("luca", "deli", 20));


setPerson spe2;

spe2.add(person("lucia", "banti", 15));
spe2.add(person("luca", "deli", 20));

test_operatoriGlobali_const_set_person(spe1, spe2);

empty_set_test();

iterator_test_interi();

iterator_test_punti();

iterator_test_persone();

test_predicati();


	
  return 0;
}