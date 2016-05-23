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
				rule.add(takeEl(str));
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

	public int sameAssum(ArrayList<String> assum){
		boolean canHave = true;
		for(int i = 0; i < _rules.size(); ++i){
			canHave = true;
			int j;
			for(j = 0;
				j < _rules.get(i).size() && canHave && !_rules.get(i).get(j).equals("=.");
				++j
			){
				if(!_rules.get(i).get(j).equals(assum)){
					canHave=!canHave;
				}
			}
			if(j < _rules.get(i).size() && canHave && !_rules.get(i).get(j).equals("=."))
				return i;
		}
		return -1;
		
	}
	public int sameProp(String prop){
		for(int i = 0; i < _rules.size() ; ++i){
			if(_rules.get(_rules.size()-1).equals(prop)){
				return i;
			}
		}
		return -1;
	}

	public boolean LogcOperator(ArrayList<String> rule){
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
						return  LogcOperator(aux) && 
								LogcOperator(nextOne);
					}
					if(rule.get(i+1).equals("|")){
						nextOne = new ArrayList<>(rule.subList(i+2, rule.size())) ;
						return  LogcOperator(aux) || 
								LogcOperator(nextOne);
					}
				}else{
					aux = new ArrayList<>(rule.subList(1,i));
					return LogcOperator(aux);
				}
			}
			if(rule.get(1).equals("&")){
				String aux = rule.get(0);
				nextOne = new ArrayList<>(rule.subList(2, rule.size())) ;
				return  isFact(aux) && 
						LogcOperator(nextOne);
			}
			if(rule.get(1).equals("|")){
				String aux = rule.get(0);
				nextOne = new ArrayList<>(rule.subList(2, rule.size())) ;
				return  !(!isFact(aux) &&
						!LogcOperator(nextOne));
			}
			if(rule.get(1).equals("=.")){
				return isFact(rule.get(0));
			}
		}
		if(rule.size() == 1) return isFact(rule.get(0));
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
//	public int ShootRule() {
//		
//	}
}
