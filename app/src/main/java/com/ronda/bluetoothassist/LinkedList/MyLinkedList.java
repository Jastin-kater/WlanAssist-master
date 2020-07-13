package com.ronda.bluetoothassist.LinkedList;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import androidx.annotation.NonNull;

/*
 *
 *类描述：
 *创建人：R
 *创建时间：${DATA}10:35
 *
 */public class MyLinkedList<T> implements Iterable<T> {

     private int thesize;
     private int modcount =0 ;
     private Node<T> beginMarker;
     private Node<T> endMarker;
    @NonNull
    @Override
    public Iterator iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T[]>
    {
        private Node<T> current = beginMarker.next;
        private int expectedModcount = modcount;
        private boolean okToRemove = false;
        @Override
        public boolean hasNext() {
            return current != endMarker;
        }

        @Override
        public T[] next() {
            if(modcount != expectedModcount)
                throw new ConcurrentModificationException();
            if(!hasNext())
                throw new NoSuchElementException();
            T[] nextItem = current.data;
            current =current.next;
            okToRemove = true;
            return nextItem;
        }

        @Override
        public void remove() {
            if (modcount != expectedModcount)
            throw new ConcurrentModificationException();
            if(!okToRemove)
                throw new IllegalStateException();

            MyLinkedList.this.remove(current.prev);
            expectedModcount++;
            okToRemove = false;
        }
    }
    private static class Node<T>
    {
        public Node(T[] d,Node<T> p,Node<T> n)
        {
            data = d;
            prev = p;
            next = n;
        }
        public T[] data;
        public Node<T> prev;
        public Node<T> next;
    }

    public MyLinkedList()
    {
    doclear();
    }

    public boolean remove( int index)
    {
        return remove(getNode(index));
    }

    private boolean remove(Node<T> node) {
        if (node == null)
            throw new NullPointerException();
        node.prev.next = node.next;
        node.next.prev = node.prev;
        thesize--;
        modcount++;
        return true;
    }

    public T[] get(int index)
    {

        return getNode(index,0,size()).data;

    }
    public T[] set(int index, T[] newval)
    {
        Node<T> p = getNode(index,0,size());
        T[] oldval = p.data;
        p.data = newval;
        return  oldval;
    }
    public boolean add(T[] x)
    {
        return add(size(),x);
    }

    private boolean add(int index, T[] x) {

        return addbefore(getNode(index,0,size()),x);
    }

    private boolean addbefore(Node<T> node, T[] x) {
        if(node == null && size() == 0) {
            System.out.println("jinru_addbefore");
            Node<T> newnode_first = new Node<>(x, beginMarker, endMarker);
            beginMarker.next = newnode_first;
            endMarker.prev = newnode_first;
            thesize++;
            modcount++;
            return  true;
        }
        if (node == null && size() != 0)
            return false;
        Node<T> newnode = new Node<>(x,node.prev,node);
        newnode.prev.next = newnode;
        newnode.next.prev = newnode;
        thesize++;
        modcount++;
        return  true;
    }


    private Node<T> getNode(int idx){

        return getNode(idx,0,size()-1);
    }
    private Node<T> getNode(int index,int min,int max) {
        Node<T> p =  beginMarker.next;
//        int index,min,max;
//        if(val.length == 1)
//        {
//            index = val[0];
//            min = 0;
//            max = size();
//        }
//        else
//        {
//            index = val[0];
//            min = val[1];
//            max = val[2];
//        }
        if(index < min || index > max)
            throw new IndexOutOfBoundsException();
        if(index < size()/2)
        {
            p = beginMarker.next;
            for(int i = 0 ; i < index ; i++)
            p = p.next;
        }
        if(index > size() / 2)
        {
            p = endMarker;
            for (int i = size() ; i > index ;i--)
                p = p.prev;
        }
            return p;
    }

    public boolean isEmpty()
    {
        return size() == 0;
    }

    public int size() {
        return thesize;
    }

    public void clear()
    {
        doclear();
    }

    private void doclear() {
  beginMarker = new Node<T>(null,null,null);
  endMarker = new Node<T>(null,beginMarker,null);
  beginMarker.next =endMarker;

  thesize = 0;
  modcount ++;

    }

}
