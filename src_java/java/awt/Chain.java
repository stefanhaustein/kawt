package java.awt;

class Chain {

    Object element;
    Chain next;

    Chain (Object element, Chain next) {
	this.element = element;
	this.next = next;
    }


    Chain remove (Object rm) {
	if (element == rm) 
	    return next;

	next = next.remove (rm);
	return this;
    }    

    

}
