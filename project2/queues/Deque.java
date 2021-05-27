import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Deque.Node<Item> first;
    private Deque.Node<Item> last;
    private int n; // size of the deque

    // helper linked list class
    private static class Node<Item> {
        private Item item;
        private Deque.Node<Item> next;
        private Deque.Node<Item> pre;
    }



    /**construct an empty deque
     *
     */
    public Deque(){
        first = new Node<>();
        last=new Node<>();
        first.next=last;
        last.pre=first;
        n=0;
    }


    /**
     * is the deque empty?
     * @return
     */
    public boolean isEmpty(){
        return n==0;
    }



    /**
     * return the number of items on the deque
     * @return
     */
    public int size(){
        return n;
    }


    /**
     * add the item to the front
     * @param item
     */
    public void addFirst(Item item){

        Node<Item> oldfirst=first;
        first=new Node<>();
        first.item=item;
        if(isEmpty()){
            last=first;
        }
        else {
            first.next=oldfirst;
            oldfirst.pre=first;
        }
        n+=1;

    }

    /**
     * add the item to the back
     * @param item
     */
    public void addLast(Item item){
        Node<Item> oldlast=last;
        last=new Node<>();
        last.item=item;
        if(isEmpty()){
            first=last;
        }
        else {
            last.pre=oldlast;
            oldlast.next=last;
        }

        n+=1;
    }


    /**
     * remove and return the item from the front
     * @return
     */
    public Item removeFirst(){
        if (isEmpty()){
            throw new NoSuchElementException("Stack underflow");
        }
        //Save the first item
        Item item=first.item;
        first=first.next;
        n-=1;
        if(isEmpty()){
            last=null;
        }

        return item;
    }


    /**
     * remove and return the item from the back
     * @return
     */
    public Item removeLast(){
        if (isEmpty()){
            throw new NoSuchElementException("Stack underflow");
        }
        //Save the last node
        Item item=last.item;
        last=last.pre;
        if(last!=null){
            last.next=null;
        }
        n-=1;
        if(isEmpty()){
            first=null;
        }
        return item;
    }


    /**
     * return an iterator over items in order from front to back
     * @return
     */
    @Override
    public Iterator<Item> iterator(){
        return new LinkedIterator(first);
    }

    private class LinkedIterator implements Iterator<Item> {
        private Deque.Node<Item> current;

        public LinkedIterator(Deque.Node<Item> first) {
            current = first;
        }
        @Override
        public boolean hasNext() {
            return current != null ;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Item next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
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


    public static void main(String[] args){
        Deque deque=new Deque();
        deque.addFirst("4");
        deque.removeFirst();
        deque.addFirst("5");
        deque.addFirst(6);
        System.out.println(deque.toString());


    }

}
