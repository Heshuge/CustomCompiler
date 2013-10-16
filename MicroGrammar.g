// Define a grammar called Little
grammar MicroGrammar;


/*------------------------------------------------------------------
 * TOKENS
 *------------------------------------------------------------------*/


/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/



/* Program */

program			:	'PROGRAM' id 'BEGIN' body 'END' {SymbolHashStack.popHash();};

id				:	IDENTIFIER;

body			:	{SymbolHashStack.newGlobal();} decl {SymbolHashStack.printHash();} functions_list;

decl			:	(string_list+ | variable_list+)*;



/* Global String Declaration */

string_list		:	'STRING' id ':=' string ';' {SymbolHashStack.newSymbol("STRING", $id.text, $string.text);};

string			:	STRINGLITERAL;




/* Variable Declaration */

variable_list	:	variable_type id_list ';' {SymbolHashStack.newSymbol($variable_type.text, $id_list.text, null);};

variable_type	:	('FLOAT' | 'INT' | 'VOID');

id_list			:	id id_tail;

id_tail			:	(',' id)*;






/* Functions */

functions_list	:	function_decl*;

function_decl	:	function_head function_body function_foot;

parameters_list	:	variable_type id {SymbolHashStack.newSymbol($variable_type.text, $id.text, null);} p_list_tail;

p_list_tail		:	(',' parameters_list)*;

// Function Format 

function_head	:	'FUNCTION' variable_type id {SymbolHashStack.newFunction($id.text);} ;

function_body	:	'(' parameters_list? ')' 'BEGIN' decl {SymbolHashStack.printHash();} statement_list;

function_foot	:	'END' {SymbolHashStack.popHash();};






/* Statement List */

statement_list	:	statement* ;

statement		:	assignment_s | read_s | write_s | return_s | if_stmt | do_while_stmt ;

// Basic Statements

read_s			:	'READ' '(' id_list ')' ';';

write_s			:	'WRITE' '(' id_list ')' ';';

return_s		:	'RETURN' expression ';';

assignment_s	:	id ':=' expression ';' {SymbolHashStack.addIRId($id.text);}{SymbolHashStack.printIRNode();};

// Complex Statements

if_stmt			:	if_stmt_head if_stmt_body if_stmt_foot;

// If Statement Format

if_stmt_head	:	'IF' '(' cond ')' {SymbolHashStack.newBlock();} {SymbolHashStack.printHash();};

if_stmt_body	:	decl statement_list else_if;

if_stmt_foot	:	'ENDIF' {SymbolHashStack.popHash();};

else_if			:	( {SymbolHashStack.newBlock();} 'ELSIF' '(' cond ')' decl {SymbolHashStack.printHash();} {SymbolHashStack.popHash();} statement_list else_if )?;

else_s			:	{SymbolHashStack.newBlock();} 'ELSE' decl {SymbolHashStack.printHash();} {SymbolHashStack.popHash();} statement_list;

// Conditionals

cond			:	( expression compop expression ) | 'TRUE' | 'FALSE';

compop			:	'<' | '>' | '=' | '!=' | '<=' | '>=';

do_while_stmt	:	do_while_head do_while_body do_while_foot;

// Do While Statment Format

do_while_head	:	'DO' {SymbolHashStack.newBlock();};

do_while_body	:	decl {SymbolHashStack.printHash();} statement_list 'WHILE' '(' cond ')';

do_while_foot	:	';' {SymbolHashStack.popHash();};






/* Expressions */

expression		:	factor expr_tail;	

factor			:	postfix_expr factor_tail;

expr_tail		:	(addop factor expr_tail )?;	

postfix_expr	:	primary | call_expr;

factor_tail		:	(mulop postfix_expr factor_tail)?;

primary			:	('('expression')') | single_id;

single_id		:	(id {SymbolHashStack.newIRNodeTail("s", $id.text);}{SymbolHashStack.printIRNode();}) | (INTLITERAL {SymbolHashStack.newIRNodeTail("s", $INTLITERAL.text);}{SymbolHashStack.printIRNode();}) | (FLOATLITERAL {SymbolHashStack.newIRNodeTail("s", $FLOATLITERAL.text);}{SymbolHashStack.printIRNode();});

call_expr		:	id '(' expr_list? ')';

expr_list		:	expression expr_list_tail;

expr_list_tail	:	(',' expression expr_list_tail)?;

addop			:	'+' | '-';

mulop			:	'*' | '/';




 
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
