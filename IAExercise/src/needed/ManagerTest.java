package needed;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class ManagerTest {

	@Test
	public void testBackWardChain1(){
		Manager manager = new Manager();
		String str = "(Abacate&Banana)&vitamina=.Cebola";
		manager.readFact("Banana");
		manager.readFact("Abacate");
		manager.readFact("vitamina");
		manager.readRule(str);
		
		if(!manager.backWChain("Cebola", new ArrayList<Integer>())) fail("Just fail me");
		
	}
	@Test
	public void testBackWardChain2(){
		Manager manager = new Manager();
		String str = "(Abacate&Banana)&-vitamina=.Cebola";
		manager.readFact("Banana");
		manager.readFact("Abacate");
		manager.readFact("vitamina");
		manager.readRule(str);
		
		if(manager.backWChain("Cebola", new ArrayList<Integer>())) fail("Just fail me");
		
	}
	@Test
	public void testBackWardChain3(){
		Manager manager = new Manager();
		manager.readFact("Banana");
		manager.readFact("Abacate");
		manager.readRule("vitamina=.Cebola");
		manager.readRule("Abacate&Banana=.vitamina");
		
		if(!manager.backWChain("Cebola", new ArrayList<Integer>())) fail("Just fail me");
		
	}
	@Test
	public void testBackWardChain4(){
		Manager manager = new Manager();
		String str = "(Abacate&Banana)&Cebola=.vitamina";
		manager.readFact("Banana");
		manager.readFact("Abacate");
		manager.readFact("-vitamina");
		manager.readRule(str);
		
		if(!manager.backWChain("-Cebola", new ArrayList<Integer>())) fail("Just fail me");
		
	}
	@Test
	public void testBackWardChain5(){
		Manager manager = new Manager();
		String str = "(Abacate&Banana)&-(Cebola)=.vitamina";
		manager.readFact("Banana");
		manager.readFact("Abacate");
		manager.readFact("-vitamina");
		manager.readRule(str);
		
		if(manager.backWChain("-Cebola", new ArrayList<Integer>())) fail("Just fail me");
		
	}
	@Test
	public void testBackWardChain6(){
		Manager manager = new Manager();
		String str = "(Abacate)&-(Cebola&Banana)=.vitamina";
		manager.readFact("-Banana");
		manager.readFact("Abacate");
		manager.readFact("-vitamina");
		manager.readRule(str);
		
		if(manager.backWChain("-Cebola", new ArrayList<Integer>())) fail("Just fail me");
		
	}

//	@Test
//	public void testLogicOperator(){
//		Manager manager = new Manager();
//		ArrayList<String> auxArray = new ArrayList<>();
//		auxArray.add("(");
//		auxArray.add("Abacate");
//		auxArray.add("&");
//		auxArray.add("Banana");
//		auxArray.add(")");
//		auxArray.add("&");
//		auxArray.add("vitamina");
//		auxArray.add("&");
//		auxArray.add("-(");
//		auxArray.add("Abacate");
//		auxArray.add(")");
//		
//		manager.readFact("Banana");
//		manager.readFact("Abacate");
//		boolean x =manager.LogcOperator(auxArray);
//		System.out.println(x);
//		if(x) fail("wrong");
//		
//	}
	
	@Test
	public void testReadFact(){
		boolean x = true;
		Manager manager = new Manager();
		ArrayList<String> auxArray = new ArrayList<>();
		auxArray.add("Abacate");
		auxArray.add("|");
		auxArray.add("Banana");
		auxArray.add("&");
		auxArray.add("Cebola");
		auxArray.add("=.");
		auxArray.add("vitamina");
		
		for(int i = 0 ; i < auxArray.size(); ++i ){
			boolean y = manager.readFact(auxArray.get(i));
			if( !((!y || x)&&(!x || y))) fail("dont add");
			x=!x;
			
		}
		
	}
	
	@Test
	public void testReadRule() {
		Manager manager = new Manager();
		String str = "(Abacate&Banana)&vitamina=.Cebola";
		ArrayList<String> auxArray = new ArrayList<>();
		auxArray.add("(");
		auxArray.add("Abacate");
		auxArray.add("&");
		auxArray.add("Banana");
		auxArray.add(")");
		auxArray.add("&");
		auxArray.add("vitamina");
		auxArray.add("=.");
		auxArray.add("Cebola");
		
		if(!manager.readRule(str)) {fail("Could not read the rule");}
		
		for(int i = 0 ; i < manager._rules.get(0).size(); ++i ){
			boolean x = manager._rules.get(0).get(i).equals(auxArray.get(i));
			if(!x) fail("Wrong taken");
			System.out.println("("+manager._rules.get(0).get(i)+")"+"("+auxArray.get(i)+")" + x);
		}
		
	}

	@Test
	public void testGetEl() {
		String sentence = "Abacate|(Banana)=.vitamina";
		ArrayList<String> auxArray = new ArrayList<>();
		ArrayList<String> aux = new ArrayList<>();
		auxArray.add("Abacate");
		auxArray.add("|");
		auxArray.add("(");
		auxArray.add("Banana");
		auxArray.add(")");
		auxArray.add("=.");
		auxArray.add("vitamina");
		int cont = 0;
		
		while(!sentence.isEmpty()){
			aux.add( (new Manager()).takeEl(sentence));
			sentence = sentence.substring(aux.get(cont).length(), sentence.length());
			boolean x = aux.get(cont).equals(auxArray.get(cont));
			System.out.println("("+aux.get(cont)+")"+"("+auxArray.get(cont)+")" + x);
			if(!x) {fail("Could not read the rule");}
			
			++cont;
		}
		for(int i=0 ; i < aux.size() ;++i){
			System.out.println(aux.get(i));
		}
		
	}

	
	
}
