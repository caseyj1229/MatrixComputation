package matrixcomputation;

import java.util.*;
import java.io.File;
import java.util.Scanner;
import java.util.Arrays;

public class SparseMatrix {

    //implement all pre-defined methods below and add your own methods as needed
    private Node[] rowHeads;
    private Node[] colHeads;
    private int size;

    public SparseMatrix(Node[] r, Node[] c) {
        rowHeads = r;
        colHeads = c;
    }

    public static SparseMatrix initializeByInput(File file) {
        SparseMatrix result = null;
       	ArrayList<Integer> vals = new ArrayList<Integer>();
	try{
	    Scanner scanner = new Scanner(file);
	    int i = 0;
	    while(scanner.hasNextInt()){
		vals.add(scanner.nextInt());
		i++;
	    }
        }catch(Exception e){
	    System.out.println("Error: " + e);
	}
	int n = vals.get(0) + 1;
	result =initializeHeaders(n);
	
	int i=1;
	while(i < vals.size()){
	    int rowI = vals.get(i);
	    int colI = vals.get(i+1);
	    int value = vals.get(i+2);
	    Node newNode = new Node(value,rowI,colI);
	    result.insert(newNode);
	    i+=3;
	}
	return result;
    }
    
    public static SparseMatrix[] initializeByFormula(int n) {
        SparseMatrix[] result = null;
	result = new SparseMatrix[3];
	SparseMatrix resB = null;
	SparseMatrix resC = null;
	SparseMatrix resD = null;
	int i, j;
	n++;
	resB = initializeHeaders(n);
	resC = initializeHeaders(n);
	resD = initializeHeaders(n);
	for(i = 1; i < n; i++){
	    for(j = 1; j < n; j++){
		initializeB(resB, i, j);
		initializeC(resC,i,j);
		initializeD(resD,i,j);
	    }    
	}
	result[0] = resB;
	result[1] = resC;
	result[2] = resD;
	return result;
    }

    public void print() {
	Node temp = new Node();
	int i, j;
	int n = rowHeads.length;
	for(i = 1; i < n; i++){
	    temp = rowHeads[i];
	    for(j = 1; j < n; j++){
		if(temp.rowLink.col == j){
		    System.out.printf("%.0f  ",temp.rowLink.value);
		    temp = temp.rowLink;
		}
		else{
		    System.out.printf("%d  ",0);
		}
	    }
	    System.out.println("");
	}
	
    }

    //parameter m --> another sparse matrix m
    public SparseMatrix add(SparseMatrix m) {
        SparseMatrix result = null;
	double sum = 0;
	result = initializeHeaders(rowHeads.length);
	int i;
	for(i=1;i<rowHeads.length;i++){
	    Node temp = rowHeads[i].rowLink;
	    Node temp2 = m.rowHeads[i].rowLink;
	    while(temp.col != 0 || temp2.col != 0){
		if(temp.col == 0){
		    Node newVal = new Node(temp2.value,i,temp2.col);
		    result.insert(newVal);
		    temp2 = temp2.rowLink;
		}
		else if(temp2.col == 0){
		    Node newVal = new Node(temp.value,i,temp.col);
		    result.insert(newVal);
		    temp = temp.rowLink;
		}
		else if(temp.col == temp2.col){
		    sum = temp.value + temp2.value;
		    Node newVal = new Node(sum,i,temp.col);
		    result.insert(newVal);
		    temp = temp.rowLink;
		    temp2 = temp2.rowLink;
		}
		else if(temp2.col < temp.col){
		    Node newVal = new Node(temp2.value,i,temp2.col);
		    result.insert(newVal);
		    temp2 = temp2.rowLink;
		}
		else{
		    Node newVal = new Node(temp.value,i,temp.col);
		    result.insert(newVal);
		    temp = temp.rowLink;
		}
	    }
	}
	return result;
    }

    //parameter m --> another sparse matrix m
    public SparseMatrix subtract(SparseMatrix m) {
        SparseMatrix result = null;
	double diff = 0;
	result = initializeHeaders(rowHeads.length);
	int i;
	for(i=1;i<rowHeads.length;i++){
	    Node temp = rowHeads[i].rowLink;
	    Node temp2 = m.rowHeads[i].rowLink;
	    while(temp.col != 0 || temp2.col != 0){
		if(temp.col == 0){
		    Node newVal = new Node(((-1)*temp2.value),i,temp2.col);
		    result.insert(newVal);
		    temp2 = temp2.rowLink;
		}
		else if(temp2.col == 0){
		    Node newVal = new Node(temp.value,i,temp.col);
		    result.insert(newVal);
		    temp = temp.rowLink;
		}
		else if(temp.col == temp2.col){
		    diff = temp.value - temp2.value;
		    Node newVal = new Node(diff,i,temp.col);
		    result.insert(newVal);
		    temp = temp.rowLink;
       		    temp2 = temp2.rowLink;
		}
		else if(temp2.col < temp.col){
		    Node newVal = new Node(((-1)*temp2.value),i,temp2.col);
		    result.insert(newVal);
		    temp2 = temp2.rowLink;
		}
		else{
		    Node newVal = new Node(temp.value,i,temp.col);
		    result.insert(newVal);
		    temp = temp.rowLink;
		}
	    }
	}
	return result;
 }

    //integer a
    public SparseMatrix scalarMultiply(int a) {
        SparseMatrix result = null;
	result = initializeHeaders(rowHeads.length);
	for(int i = 1;i<rowHeads.length;i++){
	    Node temp = rowHeads[i].rowLink;
	    while(temp != rowHeads[i]){
		double val = a * temp.value;
		Node newVal = new Node(val,i,temp.col);
		result.insert(newVal);
		temp = temp.rowLink;
	    }
	}
        return result;
    }

    //parameter m --> another sparse matrix m
    public SparseMatrix matrixMultiply(SparseMatrix m) {
        SparseMatrix result = null;
	result = initializeHeaders(rowHeads.length);
	for(int i = 1;i < rowHeads.length; i++){
	    for(int j = 1; j < rowHeads.length; j++){
		Node temp = rowHeads[i].rowLink;
		Node temp2 = m.colHeads[j].colLink;
		double sum = 0;
		while(temp != rowHeads[i] && temp2 != m.colHeads[j]){
		    if(temp.col == temp2.row && temp.row == i && temp2.col == j){
			sum += temp.value*temp2.value;
			temp = temp.rowLink;
			temp2 = temp2.colLink;
		    }
		    else if(temp.col < temp2.row){
			temp = temp.rowLink;
		    }
		    else{
			temp2 = temp2.colLink;
		    }
		}
		Node res = new Node(sum,i,j);
		result.insert(res);
	    }
	}
        return result;
    }

    //integer c
    public SparseMatrix power(int c) {
        SparseMatrix result = null;
	result = new SparseMatrix(rowHeads,colHeads);
	SparseMatrix test = result;
	String b = Integer.toBinaryString(c);
	int g = 0;
	for(int i = (b.length()-1);i >= 0; i--){
	    if(b.charAt(i)=='1' && i != (b.length()-1)){
		result = result.matrixMultiply(test);
		g++;
	    }
	    test = test.matrixMultiply(test);
	    g++;
	}
	return result;
    }
    
    //transpose matrix itself
    public SparseMatrix transpose() {
        SparseMatrix result = null;
	Node temp = new Node();
	result = initializeHeaders(rowHeads.length);
	for(int i = 1; i < rowHeads.length; i++){
	    temp = rowHeads[i].rowLink;
	    while(temp != rowHeads[i]){
		Node temp2 = new Node(temp.value,temp.col,temp.row);
		result.insert(temp2);
		temp = temp.rowLink;
	    }
	}
        return result;
    }

    public int getSize(){
	int size = (rowHeads.length - 1);
        return size;
    }
    //Insert Node into Matrix
    public void insert(Node d){
	if(d.value == 0){
	    return;
	}
	Node temp = new Node();
	Node temp2 = new Node();
	int i = 0;
	temp = rowHeads[d.row];
	if(temp.rowLink.col < d.col){
	    while(temp.rowLink != rowHeads[d.row] && temp.rowLink.col < d.col){
		temp = temp.rowLink;
		i++;
	    }
	}
	d.rowLink = temp.rowLink;
	temp.rowLink = d;
	
	temp2 = colHeads[d.col];
	if(temp2.colLink.row < d.row){
	    while(temp2.colLink != colHeads[d.col] && temp2.colLink.row < d.row){
		temp2 = temp2.colLink;
	    }
	}
	d.colLink = temp2.colLink;
	temp2.colLink = d;
    }
    
    public static SparseMatrix initializeHeaders(int n){
	Node[] r = new Node[n];
	Node[] c = new Node[n];
	int m = 0;
	while(m < n){
	    Node rowNode = new Node(0,m,0);
	    Node colNode = new Node(0,0,m);
	    r[m]=rowNode;
	    c[m]=colNode;
	    m++;
	}
	for(int j = 1;j <= (n-1);j++){
		r[j].rowLink = r[j];
		c[j].colLink = c[j];
	}
	SparseMatrix result = new SparseMatrix(r,c);
	return result;
    }

    public static void initializeB(SparseMatrix res, int i, int j){
	if(i==j){
	    int valueI = i;
	    Node newNode = new Node(valueI,i,j);
	    res.insert(newNode);
	}
    }

    public static void initializeC(SparseMatrix res, int i, int j){
	int m = (j+1)%(res.getSize());
	if(i == m){
	    int valueI = (-2*j)-i;
	    Node newNode = new Node(valueI,i,j);
	    res.insert(newNode);
	}
    }

    public static void initializeD(SparseMatrix res, int i, int j){
	if(j == 3){
	    int valueI = (i*-1);
	    Node newNode = new Node(valueI,i,j);
	    res.insert(newNode);
	}
	else if( (i%2) != 0 && (j%2) != 0){
	    int valueI = (i+j);
	    Node newNode = new Node(valueI,i,j);
	    res.insert(newNode);
	}
    }
}