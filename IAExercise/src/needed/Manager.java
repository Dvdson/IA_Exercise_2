package needed;

import java.util.ArrayList;

public class Manager {

	protected ArrayList<String> _facts;
	protected ArrayList<ArrayList<String>> _rules;

	public Manager() {
		super();
		_facts = new ArrayList<>();
		_rules = new ArrayList<>();
	}

	public boolean readFact(String str){
		str = str.replaceAll(" ", "");
		if( str.contains("=.")	||
				str.contains("&") 	||
				str.contains("|")	||
				str.contains("(")	||
				str.contains(")")
				){
			return false;
		}
		_facts.add(str);
		return true;

	}
	public boolean readRule(String str){
		str = str.replaceAll(" ", "");
		ArrayList<String> rule = new ArrayList<>();
		if(str.contains("=.")){
			int cont = 0;
			while(!str.isEmpty()){
				String element = takeEl(str);
				
				while(element.equals("(")){
					
					int i = 1, j = 0;
					while(i > 0 && j < str.length()){
						if(str.startsWith("(", j)) ++i;
						if(str.startsWith((")"), j))--i;
						 ++j;
					}
					if(j >= str.length()) return false;
					str = str.substring(0,j) + str.substring(j+1);
					element = takeEl(str);
				}
				
				rule.add(element);
				if(rule.get(cont) == "Theres is a error")
					return false;
				str = str.substring(rule.get(cont).length(),str.length());
				++cont;
			}
		}else return false;

		_rules.add(rule);
		return true;
	}

	public String takeEl(String sentence){
		String aux;
		if(sentence.startsWith("=.")){
			aux = sentence.substring(0,2);
			return aux;

		}else if(sentence.startsWith("&") || sentence.startsWith("|")){
			aux = sentence.substring(0,1);
			return aux;

		}else if(sentence.startsWith("(",0)){
			aux = sentence.substring(0,1);
			return aux;
		}else if(sentence.startsWith(")",0)){
			aux = sentence.substring(0,1);
			return aux;
		}else{
			for(int i = 0; i < sentence.length();++i){
				if(sentence.startsWith("=.", i)){
					aux = sentence.substring(0,i);
					return aux;
				}
				if(sentence.startsWith("&",i) || sentence.startsWith("|",i)){
					aux = sentence.substring(0,i);
					return aux;
				}
				if(sentence.startsWith("(",i) || sentence.startsWith(")",i)){
					aux = sentence.substring(0,i);
					return aux;
				}
			}
		}
		if(sentence.isEmpty()){
			return "";
		}
		aux = sentence.substring(0);
		return aux;
	}

	public int infe(ArrayList<String> rule){
		int i;
		for(i = 0; i+1 < rule.size() && !rule.get(i).equals("=."); ++i);
		if(i+1 >= rule.size()) return -1;
		return i;

	}

	public boolean backWChain(String element, ArrayList<Integer> notShowRule) {

		if(isFact(element))
			return true;
		if(isFact(neg(element)))
			return false;

		for (int i = 0; i < _rules.size(); i++) {		
			
			if(!notShowRule.contains(i)) {
				int infe = -1;
				int position = -1;
				ArrayList<String> rule = new ArrayList<>(_rules.get(i));
				
				for (int j = 0; j < rule.size(); j++) {
				
					if(rule.get(j).equals("=.")) infe = j;
					if(rule.get(j).equals(element)&& position==-1) position = j;
					if(rule.get(j).equals(neg(element))&& position==-1) position = j;
				}
				if(position != -1){
	
					boolean b = true;
					ArrayList<Integer> list = new ArrayList<>(notShowRule);
					ArrayList<String> aux = new ArrayList<String>(rule.subList(0,infe));
					list.add(i);
					String S;
					aux.add(neg(rule.get(infe+1)));
					rule = new ArrayList<String>(rule.subList(0, infe));
					
					
					if(position < infe) {
						Integer par = parenEntry(rule, position);
						
						if(par%2 == 0){
							S=neg(element);
						}else{
							S=element;
						}
						
						for(int k = 0 ;k <= par; k++){
							int parIn = rule.indexOf("-(");
							int parOut = rule.lastIndexOf(")");
							
							if(k%2 == 0){
								for(int j = 0, parCont = 0; j < rule.size();++j){
									if(rule.get(j).equals("-(")) ++parCont;
									if(rule.get(j).equals(")")) --parCont;
									
									if(parCont == 0 && !rule.get(j).equals(element)){
										if(isVar(rule.get(j))){
											aux.add(rule.get(j));
										}
									}
									
								}
							}
							
							rule = new ArrayList<String>(rule.subList(parIn + 1, parOut));
						}
						
						
					}else{
						aux.addAll(rule);
						S=element;
					}
					
					
					for(int j = 0; j < aux.size(); j++) {
						if(isVar(aux.get(j))) {
							boolean C = backWChain(aux.get(j), list);
							b = b & C;
							if(C)readFact(aux.get(j)); 
						}
					}
					
					return b;
				}
			}
		}

		return false;
	}

	public int parenEntry(ArrayList<String> rule, int pos){
		int cont = 0;
		while(pos > 0){
			
			if(rule.get(pos).equals("-(")){
				++cont;
			}else if(rule.get(pos).equals(")")){
				--cont;
			}
			--pos;
		}
		
		return cont;
	}
	
	public boolean isVar(String string) {

		if(string.equals("=.")) return false;
		if(string.equals("&")) return false;
		if(string.equals("|")) return false;
		return true;
	}

	public boolean LogcOperator(ArrayList<String> theRule){
		boolean neg = false;
		ArrayList<String> rule = new ArrayList<>(theRule);
		if(rule.get(0).startsWith("-")){
			neg = !neg;
			rule.set(0, neg(rule.get(0)));
		}
		ArrayList<String> nextOne = new ArrayList<>();
		if(rule.size() > 1){
			if(rule.get(0).equals("(")){
				int parCont = 1;
				ArrayList<String> aux;
				int i;
				for(i = 0; i < rule.size() && parCont > 0; ++i){
					if(rule.get(i+1).equals(")")) --parCont;
					if(rule.get(i+1).equals("(")) ++parCont;
				}
				//needs to be exception.
				if(parCont != 0) return false;

				if(i + 1 < rule.size()){
					aux = new ArrayList<>(rule.subList(1,i));
					if(rule.get(i+1).equals("&")){
						nextOne = new ArrayList<>(rule.subList(i+2, rule.size())) ;
						return  LogcOperator(aux) ^ neg && 
								LogcOperator(nextOne);
					}
					if(rule.get(i+1).equals("|")){
						nextOne = new ArrayList<>(rule.subList(i+2, rule.size())) ;
						return  LogcOperator(aux) ^ neg || 
								LogcOperator(nextOne);
					}
				}else{
					aux = new ArrayList<>(rule.subList(1,i));
					return LogcOperator(aux) ^ neg;
				}
			}
			if(rule.get(1).equals("&")){
				String aux = rule.get(0);
				nextOne = new ArrayList<>(rule.subList(2, rule.size())) ;
				return  isFact(aux) ^ neg && 
						LogcOperator(nextOne);
			}
			if(rule.get(1).equals("|")){
				String aux = rule.get(0);
				nextOne = new ArrayList<>(rule.subList(2, rule.size())) ;
				return  !(!isFact(aux) ^ neg &&
						!LogcOperator(nextOne));
			}
			if(rule.get(1).equals("=.")){
				return isFact(rule.get(0)) ^ neg;
			}
		}
		if(rule.size() == 1) return isFact(rule.get(0)) ^ neg;
		return false;
	}
	public boolean modusPonens(ArrayList<String> rule){
		int i;
		for(i = 0; i < rule.size() || !rule.get(i).equals("=."); ++i);
		if(i < rule.size()){
			return LogcOperator((ArrayList<String>)rule.subList(0, i));
		}else{
			return false;
		}
	}
	public boolean modusTonens(ArrayList<String> rule){
		int i;
		for(i = 0; i < rule.size() || !rule.get(i).equals("=."); ++i);
		if(i < rule.size()){
			return isFact(neg(rule.get(i+1)));
		}else{
			return false;
		}
	}

	public boolean ruleContains(String str, int rule){
		for(int i = 0; i < _rules.get(rule).size(); ++i)
			if(!_rules.get(rule).get(i).equals(str)) return true;
		return false;
	}
	public boolean isNeg(String a){
		return a.startsWith("-");
	}
	public String neg(String a){
		if(isNeg(a)) return a.substring(1);
		return "-" + a;
	}
	public boolean isFact(String a){
		for(int i = 0; i < _facts.size(); ++i){
			if(_facts.get(i).equals(a)) return true;
		}
		return false;
	}
}
