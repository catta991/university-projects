;;; -*- Mode: Lisp -*-

;;; Cattaneo Alessandro 851612
;;; Riva Andrea 838845
;;; Inajjar Adameddine 859260

;;; mst.lisp


(defparameter *graphs* (make-hash-table :test #'equal))
(defparameter *vertices* (make-hash-table :test #'equal))
(defparameter *arcs* (make-hash-table :test #'equal))
(defparameter *heaps* (make-hash-table :test #'equal))
(defparameter *vertex-keys* (make-hash-table :test #'equal))
(defparameter *previous* (make-hash-table :test #'equal))
(defparameter *visited* (make-hash-table :test #'equal))



(defun is-graph (graph-id)
  (gethash graph-id *graphs*))

(defun new-graph (graph-id)
  (or (gethash graph-id *graphs*)
      (setf (gethash graph-id *graphs*)
            graph-id)))


(defun delete-graph (graph-id)
  (remhash graph-id *graphs*)
  (maphash
   (lambda (key value)
     (when (equal graph-id (nth 1 value)) (remhash key *vertices*)))
   *vertices*)
  (maphash
   (lambda (key value)
     (when (equal graph-id (nth 1 value)) (remhash key *arcs*)))
   *arcs*))


(defun new-vertex (graph-id vertex-id)
  (if (gethash (list 'vertex graph-id vertex-id) *vertices*)
      (error "the vertex ~S already exists" vertex-id)
    (if (gethash graph-id *graphs*)
        (setf (gethash (list 'vertex graph-id vertex-id)
                       *vertices*)
              (list 'vertex graph-id vertex-id))
      (error "~S does not exist" graph-id))))


(defun graph-vertices (graph-id)
  (let ((vertex-rep-list '()))
    (maphash
     (lambda (key value)
       (when (equal graph-id (nth 1 key)) (push value vertex-rep-list)))
     *vertices*)
    vertex-rep-list))


(defun new-arc (graph-id vertex-id1 vertex-id2 &optional (weight 42))
  (if (and (gethash graph-id *graphs*)
           (gethash (list 'vertex graph-id vertex-id1) *vertices*)
           (gethash (list 'vertex graph-id vertex-id2) *vertices*)
           (numberp weight))
      (swap-arc graph-id vertex-id1 vertex-id2 weight)
    (error "the graph/vertex-id1/vertex-id2 you chose as argument are
  not initialized")))


(defun swap-arc (graph-id vertex-id1 vertex-id2 weight)
  (maphash
   (lambda (key value)
     (when (or (and (equal vertex-id1 (nth 2 value))
                    (equal vertex-id2 (nth 3 value))
                    (equal graph-id (nth 1 value)))
               (and (equal vertex-id1 (nth 3 value))
                    (equal vertex-id2 (nth 2 value))
                    (equal graph-id (nth 1 value))))
       (remhash key *arcs*)))
   *arcs*)
  (setf (gethash (list 'arc graph-id vertex-id1 vertex-id2 weight)
                 *arcs*)
        (list 'arc graph-id vertex-id1 vertex-id2 weight)))


(defun graph-arcs (graph-id)
  (let ((arc-rep-list '()))
    (maphash
     (lambda (key value)
       (when (equal graph-id (nth 1 key)) (push value arc-rep-list)))
     *arcs*)
    arc-rep-list))


(defun graph-vertex-neighbors (graph-id vertex-id)
  (let ((arc-rep-list))
    (maphash
     (lambda (key value)
       (when (and (equal graph-id (nth 1 key))
                  (equal vertex-id (nth 2 key))) 
         (push (list 'arc graph-id vertex-id (nth 3 value)
		     (nth 4 value))

               arc-rep-list)))
     *arcs*)
    (maphash
     (lambda (key value)
       (when (and (equal graph-id (nth 1 key))
                  (equal vertex-id (nth 3 key))) 
         (push (list 'arc graph-id vertex-id (nth 2 value)
		     (nth 4 value))
               arc-rep-list)))
     *arcs*)
    arc-rep-list))


(defun graph-vertex-adjacent (graph-id vertex-id)
  (let ((vertex-rep-list '()))
    (maphash
     (lambda (key value)
       (when (and (equal graph-id (nth 1 key))
                  (equal vertex-id (nth 2 key))) 
         (push (list 'vertex graph-id (nth 3 value)) 
               vertex-rep-list)))
     *arcs*)
    (maphash
     (lambda (key value)
       (when (and (equal graph-id (nth 1 key))
                  (equal vertex-id (nth 3 key))) 
         (push (list 'vertex graph-id (nth 2 value)) 
               vertex-rep-list)))
     *arcs*)
    vertex-rep-list))
 

(defun graph-print (graph-id)
  (let ((print-list '())) 
    (maphash
     (lambda (key value)
       (when (equal graph-id (nth 1 key)) (push value print-list)))
     *arcs*)
    (maphash
     (lambda (key value)
       (when (equal graph-id (nth 1 key)) (push value print-list)))
     *vertices*)
    print-list))


(defun new-heap (heap-id &optional (capacity 42))
  (or (gethash heap-id *heaps*)
      (setf (gethash heap-id *heaps*)
            (list 'heap heap-id 0 (make-array capacity)))))


(defun is-heap (heap-id)
  (gethash heap-id *heaps*))


(defun heap-id (heap-rep)
  (nth 1 heap-rep))


(defun heap-size (heap-rep)
  (nth 2 heap-rep))


(defun heap-actual-heap (heap-rep)
  (nth 3 heap-rep))


(defun heap-delete (heap-id)
  (remhash heap-id *heaps*)
  T)


(defun heap-empty (heap-id)
  (and (is-heap heap-id)
       (= (heap-size (gethash heap-id *heaps*))
          0)))
 

(defun heap-not-empty (heap-id)
  (and (is-heap heap-id)
       (unless (heap-empty heap-id)
         T)))


(defun heap-head (heap-id)
  (and (is-heap heap-id)
       (aref (heap-actual-heap (gethash heap-id *heaps*)) 
             1)))


(defun heap-insert (heap-id k v)
  (and (is-heap heap-id)
       (cond
        ((= (heap-size (gethash heap-id *heaps*))
            (1- (array-total-size (heap-actual-heap
				   (gethash heap-id *heaps*)))))
         (progn 
           (adjust-array-size heap-id)
           (heap-insert heap-id k v)))
        (T (and
            (insert heap-id k v)
            (increase-heap-size heap-id)
            (heapify-bottom-up
	     heap-id
	     (heap-size
	      (gethash heap-id *heaps*))))))))


(defun heap-extract (heap-id) 
  (cond 
   ((not (is-heap heap-id))
    (error "the heap ~A does not exist" heap-id))
   (t
    (let ((output
           (aref (heap-actual-heap (gethash heap-id *heaps*))
                 1)))  
      (if (and (is-heap heap-id)
               (heap-not-empty heap-id))         
          (cond ((= 1 (heap-size (gethash heap-id *heaps*)))
                 (progn 
                   (setf (aref (heap-actual-heap (gethash heap-id *heaps*)) 
                               1)
			 nil)
		
                   (setf (nth 2 (gethash heap-id *heaps*))
			 (1- (nth 2 (gethash heap-id *heaps*))))
                   output))
                (T
                 (progn 
                   (extract heap-id)
                   (heapify-up-to-bottom heap-id 1)
                   output)))
        NIL)))))


(defun extract (heap-id)
  (swap heap-id 1 (heap-size (gethash heap-id *heaps*)))
  (setf (aref (nth 3 (gethash heap-id *heaps*))
	      (heap-size (gethash heap-id *heaps*)))
	nil)
  (setf (nth 2 (gethash heap-id *heaps*))
	(1- (nth 2 (gethash heap-id *heaps*))))
  (when (eql (heap-size (gethash heap-id *heaps*))
             (floor (/ (array-total-size
			(heap-actual-heap
			 (gethash heap-id *heaps*)))
		       2)))
    (decrease-array-size heap-id)))



(defun decrease-array-size (heap-id)
  (setf new-array (make-array
		   (+ 2 (heap-size (gethash heap-id *heaps*)))))
  (swap-array new-array
	      (heap-actual-heap (gethash heap-id *heaps*))
	      heap-id
	      1))

(defun adjust-array-size (heap-id)
  (setf new-array (make-array
		   (* 2 (array-total-size
			 (heap-actual-heap
			  (gethash heap-id *heaps*))))))
  (swap-array new-array (heap-actual-heap
			 (gethash heap-id *heaps*))
	      heap-id 1))

(defun swap-array (new-array old-array heap-id n)
  (if (= n (heap-size (gethash heap-id *heaps*)))      
      (progn
        (setf (aref new-array n) (aref old-array n))
        (setf (nth 3 (gethash heap-id *heaps*)) new-array))
    (progn
      (setf (aref new-array n) (aref old-array n))
      (swap-array new-array old-array heap-id (+ 1 n)))))
  


(defun increase-heap-size (heap-id)
  (setf (nth 2 (gethash heap-id *heaps*))
	(+ 1 (heap-size (gethash heap-id *heaps*))))
  T)


(defun insert (heap-id k v)
  (setf (aref (heap-actual-heap (gethash heap-id *heaps*))
	      (+ 1 (heap-size (gethash heap-id *heaps*))))
	(list k v))
  T)


(defun heapify-up-to-bottom (heap-id pos)
  (if (not-a-leaf heap-id pos)
      (let ((little-child (little-son heap-id pos)))
        (if  (> (nth 0 (aref (heap-actual-heap (gethash heap-id *heaps*))
			     pos))
                (nth 0 (aref (heap-actual-heap (gethash heap-id *heaps*))
			     little-child)))
            (progn 
              (swap heap-id pos little-child)
              (heapify-up-to-bottom heap-id little-child))
          T)
        )
    T))

(defun little-son (heap-id pos)
  (if (nth 0 (aref (nth 3 (gethash heap-id *heaps*))
		   (right-son pos)))
      (if (<= (nth 0 (aref (heap-actual-heap (gethash heap-id *heaps*))
			   (left-son pos)))
              (nth 0 (aref (heap-actual-heap (gethash heap-id *heaps*))
			   (right-son pos))))
          (left-son pos)
        (right-son pos))
    (left-son pos)))


(defun heapify-bottom-up (heap-id pos)
  (cond 
   ((= 1 pos) T)
   ((<= (nth 0 (aref (heap-actual-heap (gethash heap-id *heaps*))
		     (parent pos)))
        (nth 0 (aref (heap-actual-heap (gethash heap-id *heaps*))
		     pos)))  
    T)
   (T (progn
        (swap heap-id (parent pos) pos)
        (heapify-bottom-up heap-id (parent pos))))))
  

(defun parent (pos)
  (if (= pos 1)
      1
    (multiple-value-bind (f s) (floor pos 2) f)))


(defun right-son (pos)
  (+ 1 (* pos 2)))


(defun left-son (pos)
  (* pos 2))


(defun not-a-leaf (heap-id pos)
  (if (>= (/ (heap-size (gethash heap-id *heaps*)) 2) pos)
      T
    nil))



(defun swap (heap-id parent-pos son-pos)
  (let ((temporary (aref (heap-actual-heap (gethash heap-id *heaps*))
			 parent-pos)))
    (setf (aref (nth 3 (gethash heap-id *heaps*))
		parent-pos)
          (aref (nth 3 (gethash heap-id *heaps*))
		son-pos))
    (setf (aref (nth 3 (gethash heap-id *heaps*))
		son-pos)
	  temporary)))


(defun heap-print (heap-id)
  (if (is-heap heap-id) 
      (progn
        (write (heap-actual-heap (gethash heap-id *heaps*)))
        T)
    NIL))
     


(defun mst-vertex-key (graph-id vertex-id)
  (gethash (list graph-id vertex-id) *vertex-keys*))


(defun mst-previous (graph-id v)
  (gethash (list graph-id v) *previous*))



(defun mst-prim (graph-id source)
  (cond 
   ((not (is-graph graph-id))
    (error "the graph ~A does not exist" graph-id))
   ((and 
     (not (stringp source))
     (equal (find source (graph-vertices graph-id) :key 'third) nil))
    (error "the vertex ~A does not exist" source))
   (T 
    (delete-previous graph-id)
    (clrhash *visited*) 
    (new-heap graph-id)
    (prim-execute graph-id
		  source
		  (mst-set-keys graph-id source
				(graph-vertices
				 graph-id)
				0))
    
    (heap-delete graph-id) 
    NIL))) 

(defun delete-previous (graph-id)
  (maphash
   (lambda (key value)
     (when (equal graph-id (nth 0 key))
       (remhash key *previous*)))
   *previous*))

(defun prim-execute (graph-id source n) 
  (cond
   ((= n 0) T) 
   (T 
    (progn
      (if (gethash (list graph-id source) *visited*)
          T
        (insert-vertices graph-id (graph-vertex-neighbors graph-id
							  source)))
      
      (let ((head (heap-head graph-id))
            (mst-key (gethash
		      (list graph-id (nth 1 (heap-head graph-id)))
		      *vertex-keys*)))
        (if (and (heap-empty graph-id)
                 ( < 0  (gethash (list graph-id source)
				 *vertex-keys*)))
            T
          (if  (< (nth 0 head) mst-key)
              (progn
                (setf (gethash (list graph-id source) *visited*)
		      source)
                (heap-extract graph-id)
                (setf (gethash (list graph-id (nth 1 head))
			       *vertex-keys*)
		      (nth 0 head))
		
                (set-prev graph-id 
                          (nth 1 head)
                          (nth 0 head) 
                          (graph-vertex-neighbors graph-id
						  (nth 1 head)))
		
                (prim-execute graph-id (nth 1 head) (- n 1)))
            (progn
              (heap-extract graph-id)
              (prim-execute graph-id source n)))))))))


(defun set-prev (graph-id vertice weight neighbors)
  (if (and(= (nth 4 (nth 0 neighbors))  weight)
          (gethash (list graph-id (nth 3 (nth 0 neighbors)))
		   *visited*))
      (setf (gethash (list graph-id vertice) *previous*)
	    (nth 3 (nth 0 neighbors)))
    (set-prev graph-id vertice weight (rest neighbors))))
          
(defun insert-vertices (graph-id neighbors)
  (cond 
   ((null neighbors) T)
   ((check-visited graph-id (nth 3 (nth 0 neighbors)))
    (insert-vertices graph-id (rest neighbors)))
   (T (progn
        (heap-insert graph-id (nth 4 (nth 0 neighbors))
		     (nth 3 (nth 0 neighbors)))
        (insert-vertices graph-id (rest neighbors))))))

(defun mst-set-keys (graph-id source vertices n)
  (cond
   ((null vertices) n)
   ((equal (nth 2 (nth 0 vertices))
         source) 
    (progn
      (setf (gethash (list graph-id source) *vertex-keys*) 0)
      (mst-set-keys graph-id source (rest vertices) n)))
   (T (progn
        (setf (gethash (list graph-id
			     (nth 2 (nth 0 vertices)))
		       *vertex-keys*)
	      MOST-POSITIVE-DOUBLE-FLOAT)
        (mst-set-keys graph-id source (rest vertices) (+ 1 n))))))

(defun check-visited (graph-id arc)
  (if (gethash (list graph-id arc) *visited*)
      T  
    NIL))


(defun mst-get (graph-id source)
  (cond
   ((not (is-graph graph-id))
    (error "the graph ~A does not exist" graph-id))
   ((and 
     (not (stringp source))
     (equal (find source (graph-vertices graph-id) :key 'third) nil))
    (error "the vertex ~A does not exist" source))
   (t (if (= 0 (gethash (list graph-id source) *vertex-keys*))
          (list-get graph-id source (graph-vertex-neighbors graph-id
							    source))
        NIL))))

(defun list-get (graph-id source list-neighbors)
  (let ((little-son (check-son graph-id source list-neighbors)))
    (if (not (null little-son))
        (append (list (list 'arc graph-id source
			    (nth 0 little-son)(nth 1 little-son)))
                (list-get graph-id (nth 0 little-son)
			  (graph-vertex-neighbors graph-id
						  (nth 0 little-son)))
                (list-get graph-id source (subtract little-son
						    list-neighbors)))
      nil))) 

(defun subtract (element list)
  (if (equal (nth 0 element)(nth 3 (nth 0 list)))
      (rest list)
    (cons (nth 0 list) (subtract element (rest list)))))


(defun check-son (graph-id source neighbors)
  (if (null neighbors)
      nil
    (if (equal source (gethash (list graph-id (nth 3 (nth 0  neighbors)))
			     *previous*))
        (let ((list-min (check-son graph-id source (rest neighbors))))
          (new-list-min 
           (list (nth 3 (nth 0 neighbors)) 
                 (mst-vertex-key graph-id (nth 3 (nth 0 neighbors))))
           list-min))                  
      (check-son graph-id source (rest neighbors)))))


(defun new-list-min (lista-a lista-b)
  (if  (not (null lista-b))
      (if (equal (nth 1 lista-a) (nth 1 lista-b))
          (cond ((and (numberp (nth 0 lista-a))
                      (numberp (nth 0 lista-b)))
                 (if (> (nth 0 lista-a)(nth 0 lista-b)) 
                     lista-b
                   lista-a))
                (T          
                 (if (string> (write-to-string (nth 0 lista-a))
                              (write-to-string (nth 0 lista-b))) 
                     lista-b
                   lista-a)))
        (if  (< (nth 1 lista-a) (nth 1 lista-b))
            lista-a
          lista-b))
    lista-a))


;;; end of file mst




