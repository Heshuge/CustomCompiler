// Define a grammar called Little
grammar MicroGrammar;


/*------------------------------------------------------------------
 * TOKENS
 *------------------------------------------------------------------*/


/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/



/* Program */

program		:	'PROGRAM' id 'BEGIN' body 'END' {SymbolHashStack.popHash();};

id			:	IDENTIFIER;

body		:	{SymbolHashStack.newGlobal();} decl {SymbolHashStack.printHash();} functions_list;

decl			:	(string_list+ | variable_list+)*;



/* Global String Declaration */

string_list	:	'STRING' id ':=' string ';' {SymbolHashStack.newSymbol("STRING", $id.text, $string.text);};

string		:	STRINGLITERAL;




/* Variable Declaration */

variable_list	:	variable_type id_list ';' {SymbolHashStack.newSymbol($variable_type.text, $id_list.text, null);};

variable_type	:	('FLOAT' | 'INT' | 'VOID');

id_list		:	id id_tail;

id_tail		:	(',' id)*;






/* Functions */

functions_list	:	function_declaration*;

function_declaration	:	'FUNCTION' variable_type id {SymbolHashStack.newFunction($id.text);} '(' parameters_list? ')' 'BEGIN' function_body 'END' {SymbolHashStack.popHash();};

parameters_list:	variable_type id p_list_tail {SymbolHashStack.newSymbol($variable_type.text, $id.text, null);};

p_list_tail		:	(',' parameters_list)*;

function_body	:	statement_list;






/* Statement List */

statement_list	:	statement*;

statement	:	assignment_s | read_s | write_s | return_s | if_stmt | do_while_stmt;






/* Basic Statements */

read_s		:	'READ' '(' id_list ')' ';';

write_s		:	'WRITE' '(' id_list ')' ';';

return_s		:	'RETURN' expression ';';

assignment_s	:	id ':=' expression ';';

else_s		:	{SymbolHashStack.newBlock();}  'ELSE' decl {SymbolHashStack.printHash();} {SymbolHashStack.popHash();} statement_list;




/* Expressions */

expression	:	factor expr_tail;	

expr_tail		:	(addop factor expr_tail)?;	

factor		:	postfix_expr factor_tail;

factor_tail	:	(mulop postfix_expr factor_tail)?;

postfix_expr	:	primary | call_expr;

call_expr		:	id '(' expr_list? ')';

expr_list		:	expression expr_list_tail;

expr_list_tail	:	(',' expression expr_list_tail)?;

primary		:	( '('expression')' ) | id | INTLITERAL | FLOATLITERAL;

addop		:	'+' | '-';

mulop		:	'*' | '/';





/* Complex Statements & Condition */


if_stmt		:	{SymbolHashStack.newBlock();} 'IF' '(' cond ')' decl statement_list else_part {SymbolHashStack.printHash();} {SymbolHashStack.popHash();} 'ENDIF';

else_part		:	( {SymbolHashStack.newBlock();} 'ELSIF' '(' cond ')' decl {SymbolHashStack.printHash();} {SymbolHashStack.popHash();} statement_list else_part )?;

cond		:	( expression compop expression ) | 'TRUE' | 'FALSE';

compop		:	'<' | '>' | '=' | '!=' | '<=' | '>=';

do_while_stmt	:	{SymbolHashStack.newBlock();} 'DO' decl {SymbolHashStack.printHash();} statement_list 'WHILE' '(' cond ')' ';' {SymbolHashStack.popHash();};



 
/*------------------------------------------------------------------
 * LEXER RULES
 *------------------------------------------------------------------*/

KEYWORD 	: ('PROGRAM' | 'BEGIN' | 'STRING' | 'FUNCTION' | 'INT' | 'IF' | 'RETURN' | 'ELSIF' | 'ENDIF' | 'END' | 'VOID' | 'WRITE' | 'DO' | 'WHILE' | 'READ' | 'FLOAT' | 'TRUE');

OPERATOR 	: (':=' | ';' | '(' | ')' | '>' | '-' | '+' | '=' | ',' | '!=' | '/' | '*' | '<' | '<=');

IDENTIFIER 	: [a-zA-Z]+[0-9]?;

INTLITERAL 	: [0-9]+;

FLOATLITERAL 	: [0-9]+(.)[0-9]+;

STRINGLITERAL: '"'.*?'"';

WS 			: [ \t\r\n]+ -> skip;

COMMENT 	: ('--'.*?'\n')  -> skip;
