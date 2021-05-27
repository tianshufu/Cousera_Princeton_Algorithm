import java.util.Iterator;
import java.util.NoSuchElementException;
import  edu.princeton.cs.algs4.StdRandom;




public class RandomizedQueue<Item> implements Iterable<Item> {
        // initial capacity of underlying resizing array
        private static final int INIT_CAPACITY = 8;
        private  Item[] a;
        // Define the size
        private  int n;
        // Index of the first element of the que
        private  int first;
        // Index of the last element of the que
        private  int last;

        // construct an empty randomized queue
        public RandomizedQueue(){
            a=(Item[]) new Object[INIT_CAPACITY];
            n=0;
            first=0;
            last=0;
        }

        /**
        is the randomized queue empty?
         */
        public boolean isEmpty(){
            return n==0;
        }

        /**return the number of items on the randomized queue
         */
        public int size(){
            return  n;
        }

        /**

         */
         private  void  resize(int capacity){
             assert capacity>=n;
             //Copy the orignal one to a new one
             Item[] copy=(Item[]) new Object[capacity];
             for (int i=0;i<n;i++){
                 copy[i]=a[(first+i)%a.length];
             }
             a=copy;
             //Refresh first and last
             first=0;
             last=n;

         }
         /** add the item
        *
        * @param item
         */
        public void enqueue(Item item){
            if(item==null){
                throw new IllegalArgumentException();
            }
            if(n==a.length){
                resize(2*a.length);
            }
            //add item
            a[last++]=item;
            //If Reached to the end, wrap around
            if(last==a.length){
                last=0;
            }
            n++;

        }

        /**remove and return a random item
        */
         public Item dequeue(){
             if(isEmpty()){
                 throw  new NoSuchElementException();
             }
             //Generate a random num of 0-n-1
             int i =StdRandom.uniform(0,n);
             int indexI=(first+i)%a.length;
             //Swap i with the first num
             Item tmp=a[first];
             a[first]=a[indexI];
             a[indexI]=tmp;
             //Remove the first item
             Item item=a[first];
             a[first]=null;
             n--;
             first++;
             if(first==a.length){
                 first=0;
             }
             //shrink the array
             if((n>0)&(n==a.length/4)){
                 resize(a.length/2);
             }
            return item;
        }


        /** return a random item (but do not remove it)
        */
         public Item sample(){
             if(isEmpty()){
                 throw  new NoSuchElementException();
             }
             int i =StdRandom.uniform(0,n);
             return  a[(first+i)%a.length];

        }


        /** return an independent iterator over items in random order
        */
        @Override
        public Iterator<Item> iterator(){
            return new ArrayIterator();
        }


        private class ArrayIterator implements Iterator<Item> {
            private int i = 0;
            @Override
            public boolean hasNext() { return i < n; }
            @Override
            public void remove() { throw new UnsupportedOperationException();}
            @Override
            public Item next() {
                if (!hasNext()){ throw new NoSuchElementException();}
                Item item = a[(i + first) % a.length];
                i++;
                return item;
            }

        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();
            for (Item item : this) {
                s.append(item);
                s.append(' ');
            }
            return s.toString();
        }




        // unit testing (required)
        public static void main(String[] args){
            RandomizedQueue randomizedQueue=new RandomizedQueue();
            randomizedQueue.enqueue("1");
            randomizedQueue.enqueue("2");
            randomizedQueue.enqueue("3");
            randomizedQueue.dequeue();
            randomizedQueue.dequeue();
            randomizedQueue.dequeue();
            System.out.println(randomizedQueue.toString());




        }

    }

