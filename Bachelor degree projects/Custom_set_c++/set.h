#ifndef set_H
#define set_H

#include <ostream>
#include <cassert>
#include <cstddef>            // std::ptrdiff_t
#include <iterator>           // std::forward_iterator_tag
#include <stdexcept> 


/**
 
 @author Alessandro Cattaneo [851612] 

  @brief classe set

  La classe implementa una generico set di oggetti T in maniera tale che 
  non ci siano elementi duplicati.
  
  La valutazione se due dati di tipo T sono uguali è realizzata
  tramite il funtore Equal che prende due valori a e b, e ritorna
  vero se i due valori sono considerati uguali. 

*/


template <typename T, typename Equal>  
class set{


	 /**
    @brief struttura nodo

    Struttura dati node interna che viene usata per creare
    il set.  
  */

	struct node
	{
		T value;
		node *next;


		 /**
      Costruttore di default
      @post next == nullptr
    */
		node() : next(nullptr){}

		

		/**
      Costruttore secondario

      @param val valore da copiare

      @post value == val
      @post next == nullptr
    */

	explicit node(const T &val): value(val), next(nullptr){}



	/**
      Copy constructor. Copiamo i dati membro a membro.
      In questo caso essendo una struttura di suporto
      a set, non abbiamo nessun problema a condividere i dati perchè
      è un semplice contenitore di informazioni

      @param other node da copiare
    */

	node(const node &other) : value(other.value), next(other.next) {}
	

	/**
      Operatore di assegnamento. 

      @param other nodo da copiare
      @return reference al nodo this
    */
    node& operator=(const node &other) {
      value = other.value;
      next = other.next;
      return *this;
    }

    /**
      Distruttore. 
    */
    ~node() { }  // è vuoto perchè non ho creato nulla con la new


	};



  node *_head; ///< puntatore al primo nodo della lista
  



Equal  _equals; ///< funtore per l'uguaglianza tra dati T

  

unsigned int _setSize; ///< numero di elementi nel set




public:
  /**
    Costruttore di default.

    @post _head == nullptr
    @post _size == 0
    
  */
  set() : _head(nullptr),_setSize(0){}



  /**
    Copy constructor

    @param other lista da copiare

    @post _setSize = other._setSize
    
  */
  

  set(const set &other) : _head(nullptr), _setSize(0){
    
    node *current = other._head;


    try {
      while (current != nullptr) {
        add(current->value);
        current = current->next;
      }
    }
    catch(...) {
      emptySet();
      throw;
    }
  }


  /**
    Operatore di assegnamento

    @param other lista da copiare

    @return reference alla lista this

    @post _setSize = other._setSize

    @post this->head = temp.head
  */



   set& operator=(const set &other) {
    if(this!=&other) {
      set temp(other);
      std::swap(this->_head, temp._head);
      std::swap(this->_setSize, temp._setSize);     
    }
    return *this;
  }



 /**
    Distruttore 

    @post _head == nullptr
    @post _size == 0
  
  */
  ~set() {
    
    emptySet();

  }


  /**
    Svuota il set

    @post _head == nullptr
    @post _size == 0

  */
 
  void emptySet() {
    
    node *current = _head;

    while (current!=nullptr) {
      node *nextNode = current->next;
      delete current;
      current = nextNode;
    }
    
    _head = nullptr;
    _setSize=0;
  }



  /**
    Aggiunge un elemento nel set in modo tale che non
    vi siano elementi duplicati il controllo viene effettuato
    dal funtore di tipo Equal

    @param value valore da inserire nella lista

    @post _setSize = _setSize+1

    @throw std::bad_alloc possibile eccezione di allocazione
  */
  

   void add(const T& value) {
   
    node *temp = nullptr;

  try{
    	 temp = new node(value);

    	 // inserimento nel caso del set vuoto

     if (_head==nullptr) {
        _head = temp;
       _setSize = 1;
        return;
      }

       node *current = _head;
      node *predecessor = nullptr;

      while(current != nullptr) {
   
      if(_equals(current -> value, temp -> value)){

        delete temp;
        temp = nullptr;
     	  return;
	
      }

      predecessor = current;
     
      current = current -> next;

    }

    predecessor ->next = temp;
     _setSize++;

  } catch(...){
  
    std::cerr << "impossibile creare un nuovo nodo, il set non è stato modificato" <<std::endl;
    delete temp;
    temp = nullptr;
    throw;
  }

}


 /**
    Stampa la dimensione del set 

    @return dimensione attuale del set
  */ 

  const unsigned int& getSetSize() const {
    return _setSize;
  }




/**
  Se presente rimuove l'elemento con valore value dal
  set e sistema i puntatori in modo tale che il set rimanga tale 
  e non venga tagliato.

  Usa il funtore di ugiaglianza

  @pre il set non deve essere vuoto

  @param value il valore dell'elemento da rimuovere dal set


  @post se l'elemento è presente _setSize = _setSize-1
 
  */



void remove (const T& value){

   bool notFound = true;

   node *current = _head;

   node *predecessor = nullptr;

   node *toDelete = nullptr;

  
   assert(current != nullptr);


   if(_equals(current->value, value)){

      
    _head = current -> next;


      toDelete = current;


      toDelete -> next = nullptr;

      notFound = false;

      _setSize --;


    }else if(current -> next != nullptr)

    {

      predecessor = current;
      current = current ->next;

   while (current -> next != nullptr && notFound) { 

     
      if(_equals(current->value, value))
      {
          
          toDelete = current;

          predecessor->next = current ->next;

         toDelete -> next = nullptr;
     

          notFound = false;

          _setSize --;


      }else{

        predecessor = current;

        current = current -> next;
      }

    }

  }


    if(notFound){  


      if(_equals(current->value, value)){

        toDelete = current;

        predecessor -> next = nullptr;

        _setSize --;


        
      }


    }


    if(toDelete != nullptr){
    
    delete toDelete;

    toDelete = nullptr;

  }
   
    

}



  
  /**
    restiuisce il valore dell'elemto in posizione i-esima
    se l'indice non è più grande della lunghezza della lista

    Gli indici partono da 0


    @param index indice dell'elemento che si vuole ottenere

    @return un reference al valore dell'elemento i-esimo del set 

    @throws std::out_of_range se l'indice va oltre la dimensione massima del set
      
    */

  const T& operator[](unsigned int index) const{

   if(index >= _setSize) {
      throw std::out_of_range ("l'indice e' oltre la dimensione massima del set");
    }

     node *current = _head;

     while(index > 0){

        current = current -> next;

        --index;

     }
  

     return current->value;


  }



/**
  Restituisce true se i 2 set sono identici ossia hanno la stessa 
  lunghezza e contengono gli tessi valori, non per forza nello stesso 
  ordine

  @param other set da confrontare con il set corrente


  @return true se i set sono identici false altrimenti


*/

  bool operator==(const set& other) const{

    if(this -> _setSize != other._setSize)
      return false;

    set app(other);

    node *currentFirst = this -> _head;

    while(currentFirst != nullptr){

    try{

       app.add(currentFirst -> value);

       currentFirst = currentFirst -> next;

    } catch(...){

      app.emptySet();
       throw;
      }
    }


    if(app._setSize != this-> _setSize)

      return false;

    else
      return true;

  }



    /**
    Costruttore che crea una set riempito con dati
    presi da una sequenza identificata da un iteratore di 
    inizio e uno di fine. Gli iteratori possono essere di
    qualunque tipo Q. 

    @param s iteratore di inizio set
    @param e iteratore di fine set

    @throw std::bad_alloc possibile eccezione di allocazione
  */
  template <typename Q>
  set(Q s, Q e) : _head(nullptr), _setSize(0){
    try {
      for( ; s!=e; ++s)
        add(static_cast<T>(*s));
    }
    catch(...) {
      // in caso di errore di memoria si svuota il set
      emptySet();
      throw;
    }
  }




   /**
    Gli iteratori devono iterare sui dati inseriti nella classe
    principale. Siccome nel set mettiamo dei dati di 
    tipo T, l'iteratore deve iterare sui dati di tipo T. NON sui
    nodi del set che sono un dettaglio implementativo interno.
  */
  class const_iterator {

  public:
    typedef std::forward_iterator_tag iterator_category;
    typedef T                         value_type;
    typedef ptrdiff_t                 difference_type;
    typedef const T*                  pointer;
    typedef const T&                  reference;
  
    const_iterator() : ptr(nullptr) {
    }
    
    const_iterator(const const_iterator &other) : ptr(other.ptr) {
    }

    const_iterator& operator=(const const_iterator &other) {
      ptr = other.ptr;
      return *this;
    }

    ~const_iterator() { }

    // Ritorna il dato riferito dall'iteratore (dereferenziamento)
    reference operator*() const {
      return ptr->value;
    }

    // Ritorna il puntatore al dato riferito dall'iteratore
    pointer operator->() const {
      return &(ptr->value);
    }
    
    // Operatore di iterazione post-incremento
    const_iterator operator++(int) {
      const_iterator tmp(*this);
      ptr = ptr->next;
      return tmp;
    }

    // Operatore di iterazione pre-incremento
    const_iterator& operator++() {
      ptr = ptr->next;
      return *this;
    }

    // Uguaglianza
    bool operator==(const const_iterator &other) const {
      return (ptr == other.ptr);
    }
    
    // Diversita'
    bool operator!=(const const_iterator &other) const {
      return !(other == *this);
    }

  private:
    // La classe container deve essere messa friend dell'iteratore per poter
    // usare il costruttore di inizializzazione.
    friend class set; 

    // Costruttore privato di inizializzazione usato dalla classe container
    // tipicamente nei metodi begin e end
    const_iterator(const node *p) : ptr(p) { }
    
    const node * ptr;
    
  }; // classe const_iterator
  
  // Ritorna l'iteratore all'inizio della sequenza dati
  const_iterator begin() const {
    return const_iterator(_head);
  }
  
  // Ritorna l'iteratore alla fine della sequenza dati
  const_iterator end() const {
    return const_iterator(nullptr);
  }

/**
  ridefinizione dell'operatore << per set

  @param os oggetto della classe ostream usato per stampare

  @param set set il cui contenuto deve essere stampato


  @return reference all'oggetto della classe ostream passato come parametro
 
 
  */

  friend std::ostream &operator<<(std::ostream &os, 
    const set &set){

    node *current = set._head;
    while(current !=nullptr) {
      os << current->value << " ";
      current = current->next;
    }

    return os;
  }


};





/**
  Funzione GLOBALE che crea un set di soli valori 
  contenuti in un generico set che soddisfano 
  un predicato generico di tipo P.

  @param st set sorgente
  @param pred predicato da soddisfare


  @return app set che contiene solo i valori che soddisfano il predicato
*/


template <typename T, typename E, typename P>
const set<T,E> filter_out(const set<T,E> &st, P pred){
  
  set<T,E> app;
  
  typename set<T,E>::const_iterator b, e;

  for(b = st.begin(), e=st.end(); b!=e; ++b) {
    if (pred(*b)){

        try{
          app.add(*b);
        }catch(...){
          app.emptySet();
          throw;
        }

    }

  }

  return app;

}


/**
  Ridefinizione GLOBALE dell'operatore + che concatena 2 set

  @param s1 primo set da concatenare
  @param s2 secondo set da concatenare


  @return concat set concatenazione di s1 e s2
*/



template <typename T, typename E>
 set<T,E> operator+(const set<T,E> &s1, const set<T,E> &s2){


  set<T,E> concat;


  typename set<T,E>::const_iterator b, e;

  for(b = s1.begin(), e=s1.end(); b!=e; ++b) {
    
    try{
     concat.add(*b);
    
    }catch(...){
      concat.emptySet();
      throw;
    }
  

  }


  for(b = s2.begin(), e=s2.end(); b!=e; ++b) {
     
    try{
     concat.add(*b);
    }catch(...){
      concat.emptySet();
      throw;
    }
  }

  return concat;
}


/**
  Ridefinizione GLOBALE dell'operatore - che resituisce
  l'intersezione tra 2 set

  @param s1 primo set su cui effettuare l'intersezione
  @param s2 secondo set su cui effettuare l'intersezione


  @return intersection set d'intersezione tra s1 e s2
*/


template <typename T, typename E>
const set<T,E> operator-(const set<T,E> &s1, const set<T,E> &s2){

   set<T,E> intersection;

   E eql;

   typename set<T,E>::const_iterator b1, e1;
    typename set<T,E>::const_iterator b2, e2;

    for(b1 = s1.begin(), e1=s1.end(); b1!=e1; ++b1) {
      for(b2= s2.begin(), e2=s2.end(); b2!=e2; ++b2) {
          if(eql(*b1, *b2))
             
            try{
              intersection.add(*b1);
            }catch(...){
              intersection.emptySet();
              throw;
            }

        }
    }

return intersection;


}










#endif