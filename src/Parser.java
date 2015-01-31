import java.util.ArrayList;


class Parser {
	        int pos = -1, c;
	        int loc = 0;
	        public boolean Error = false;
	        public String str = "";
			public ArrayList<String> names;
			public ArrayList<String> values;
			private int lastIndex = 0;
			private char[] opperatorChars = {' ', '\n', '+','-','*','%','/','^','(',')'};
	        void eatChar() {
	            c = (++pos < str.length()) ? str.charAt(pos) : -1;
	        }

	        void eatSpace() {
	            while (Character.isWhitespace(c)) eatChar();
	        }
	        public Parser(String str)
	        {this.str = str;
	        values = new ArrayList <>();
	        names = new ArrayList <>();}
	        double parse() {
	            eatChar();
	            double v = parseExpression();
	            if (c != -1) Error = true; //throw new RuntimeException("Unexpected: " + (char)c);
	            return v;
	        }

	        // Grammar:
	        // expression = term | expression `+` term | expression `-` term
	        // term = factor | term `*` factor | term `/` factor | term brackets
	        // factor = brackets | number | factor `^` factor
	        // brackets = `(` expression `)`

	        double parseExpression() {
	            double v = parseTerm();
	            for (;;) {
	                eatSpace();
	                if (c == '+') { // addition
	                    eatChar();
	                    v += parseTerm();
	                } else if (c == '-') { // subtraction
	                    eatChar();
	                    v -= parseTerm();
	                } else {
	                    return v;
	                }
	            }
	        }

	        double parseTerm() {
	            double v = parseFactor();
	            for (;;) {
	                eatSpace();
	                if (c == '/') { // division
	                    eatChar();
	                    v /= parseFactor();
	                } else if (c == '*' || c == '(') { // multiplication
	                    if (c == '*') eatChar();
	                    v *= parseFactor();
	                } else if (c == '%') { // modulo
	                    eatChar();
	                    v %= parseFactor();
	                } else {
	                    return v;
	                }
	            }
	        }

	        double parseFactor() {
	            double v;
	            boolean negate = false;
	            eatSpace();
	            
	            if (c == '(') { // brackets
	                eatChar();
	                v = parseExpression();
	                if (c == ')') eatChar();
	            } else { // numbers
	                if (c == '+' || c == '-') { // unary plus & minus
	                    negate = c == '-';
	                    eatChar();
	                    eatSpace();
	                }
	                StringBuilder sb = new StringBuilder();
	                //System.out.println(pos);
	                for (int i = 0; i < names.size(); i++) {
	                	//System.out.println(pos == str.indexOf(names.get(i),lastIndex));
	                	//System.out.println(findOpChar(i));
	                if(pos == str.indexOf(names.get(i),lastIndex) && findOpChar(i)) {
	                	lastIndex = str.indexOf(names.get(i), lastIndex)+1;
	                	sb = new StringBuilder();
	                	sb.append(values.get(i));
	                	//System.out.println(sb.toString());
	                	if (names.get(i).length() == 0 ) eatChar();
	                	else
	                	for (int o = 0; o < names.get(i).length(); o++) eatChar();
	                	
	                }
	                	
	                }
	                while ((c >= '0' && c <= '9') || c == '.') {
	                    sb.append((char)c);
	                    eatChar();
	                }
	                
	                
	                	
	                if (sb.length() == 0){ Error = true;  v = 0;}
	                else
	                v = Double.parseDouble(sb.toString());
	            }
	            eatSpace();
	            if (c == '^') { // exponentiation
	                eatChar();
	                v = Math.pow(v, parseFactor());
	            }
	            if (negate) v = -v; // exponentiation has higher priority than unary minus: -3^2=-9
	            return v;
	        }

			private boolean findOpChar(int index) {
				for (int i = 0; i < opperatorChars.length; i++) {
					//System.out.println(pos+names.get(index).length() < str.length());
					//System.out.println(str.charAt(pos+names.get(index).length()) == opperatorChars[i]);
					if (pos+names.get(index).length() < str.length() && str.charAt(pos+names.get(index).length()) == opperatorChars[i]) return true;
				}
				if (pos+names.get(index).length() == str.length()) return true;
				return false;
			}
	    }