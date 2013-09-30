// Define a grammar called Little
grammar MicroGrammar;


/*------------------------------------------------------------------
 * TOKENS
 *------------------------------------------------------------------*/


/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/

/* Program */

program			:	'PROGRAM' id 'BEGIN' body 'END'
			;

id			:	IDENTIFIER
			;

body			:	decl functions_list
			;

decl			:	((string_list decl?) | (variable_list decl?))?
			;

/* Global String Declaration */

string_list		:	'STRING' id ':=' string ';'
			;

string			:	STRINGLITERAL
			;

/* Variable Declaration */

variable_list		:	('FLOAT' | 'INT' | 'VOID') id_list ';'
			;

id_list			:	id id_tail
			;

id_tail			:	(',' id)*
			;









/* Functions */

functions_list		:	function_declaration*
			;

function_declaration	:	'FUNCTION' ('FLOAT' | 'INT' | 'VOID') id '(' parameters_list? ')' 'BEGIN' function_body 'END'
			;

parameters_list		:	('FLOAT' | 'INT' | 'VOID') id p_list_tail
			;

p_list_tail		:	(',' parameters_list)*
			;

function_body		:	statement_list
			;




/* Statement List */

statement_list		:	statement*
			;

statement		:	assignment_s | read_s | write_s | return_s | if_s | do_while_stmt
			;






/* Basic Statements */

read_s			:	'READ' '(' id_list ')' ';'
			;

write_s			:	'WRITE' '(' id_list ')' ';'
			;

return_s		:	'RETURN' expression ';'
			;

assignment_s		:	id ':=' expression ';'
			;

if_s			:	'IF' (.)*? 'ENDIF'
			;






/* Expressions */

expression		:	factor expr_tail
			;	
expr_tail		:	(addop factor expr_tail)?
			;	
factor			:	postfix_expr factor_tail
			;
factor_tail		:	(mulop postfix_expr factor_tail)?
			;
postfix_expr		:	primary | call_expr
			;
call_expr		:	id '(' expr_list? ')'
			;
expr_list		:	expression expr_list_tail
			;
expr_list_tail		:	(',' expression expr_list_tail)?
			;
primary			:	( '('expression')' ) | id | INTLITERAL | FLOATLITERAL 
			;
addop			:	'+' | '-'
			;
mulop			:	'*' | '/'
			;



/* Complex Statements & Condition */


if_stmt			:	'IF' '(' cond ')' decl? statement_list else_part 'ENDIF'
			;
else_part		:	('ELSIF' '(' cond ')' decl? statement_list else_part)?
			;
cond			:	( expression compop expression ) | 'TRUE' | 'FALSE'
			;
compop			:	'<' | '>' | '=' | '!=' | '<=' | '>='
			;
do_while_stmt		:	'DO' decl? statement_list 'WHILE' '(' cond ')' ';'
			;


 
/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

KEYWORD : ('PROGRAM' | 'BEGIN' | 'STRING' | 'FUNCTION' | 'INT' | 'IF' | 'RETURN' | 'ELSIF' | 'ENDIF' | 'END' | 'VOID' | 'WRITE' | 'DO' | 'WHILE' | 'READ' | 'FLOAT' | 'TRUE');

OPERATOR : (':=' | ';' | '(' | ')' | '>' | '-' | '+' | '=' | ',' | '!=' | '/' | '*' | '<' | '<=');

IDENTIFIER : [a-zA-Z]+[0-9]?;

INTLITERAL : [0-9]+;

FLOATLITERAL : [0-9]+(.)[0-9]+;

STRINGLITERAL : '"'.*?'"';

WS : [ \t\r\n]+ -> skip;

COMMENT : ('--'.*?'\n')  -> skip;
