grammar CNF;

cnf: IDENTIFIER         #identifierAtom
    | '(' cnf ')'       #parenthesized
    | '!' cnf           #not
    | cnf 'v' cnf       #disjunction
    | cnf '^' cnf       #conjunction
    | cnf '->' cnf      #implication
    ;

IDENTIFIER: [a-zA-Z0-9]+;
WS : [ \t]+ -> skip ; // toss out whitespace