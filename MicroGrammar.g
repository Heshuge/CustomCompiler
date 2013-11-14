// Define a grammar called Little
grammar MicroGrammar;


/*------------------------------------------------------------------
 * TOKENS
 *------------------------------------------------------------------*/


/*------------------------------------------------------------------
 * PARSER RULES
 *------------------------------------------------------------------*/



/* Program */

program			:	'PROGRAM' id 'BEGIN' {SymbolHashStack.newGlobal(); ExprStack.globalPush();} body 'END' {SymbolHashStack.popHash();};

id				:	IDENTIFIER;

body			:	decl {SymbolHashStack.printIRCode();} functions_list {SymbolHashStack.printTinyCode(); ExprStack.printTinyList();};

decl			:	(string_list+ | variable_list+)*;



/* Global String Declaration */

string_list		:	'STRING' id ':=' string ';' {SymbolHashStack.newSymbol("STRING", $id.text, $string.text);};

string			:	STRINGLITERAL;




/* Variable Declaration */

variable_list	:	variable_type id_list ';' {SymbolHashStack.newSymbol($variable_type.text, $id_list.text, null); };

variable_type	:	('FLOAT' | 'INT' | 'VOID');

id_list			:	id id_tail;

id_tail			:	(',' id)*;






/* Functions */

functions_list	:	function_decl*;

function_decl	:	function_head function_body function_foot;

parameters_list	:	variable_type id {SymbolHashStack.newSymbol($variable_type.text, $id.text, null);} p_list_tail;

p_list_tail		:	(',' parameters_list)*;

// Function Format 

function_head	:	'FUNCTION' variable_type id {SymbolHashStack.newFunction($id.text);} '(' parameters_list? ')'{ExprStack.functionLabel($id.text, $parameters_list.text);};

function_body	:	'BEGIN' decl statement_list;

function_foot	:	'END' {SymbolHashStack.popHash(); ExprStack.functionEnd();};






/* Statement List */

statement_list	:	statement* ;

statement		:	assignment_s | read_s | write_s | return_s | if_stmt | do_while_stmt ;

// Basic Statements

assignment_s	:	id ':=' expression {ExprStack.addLIdentifier($id.text); ExprStack.evaluateExpr();} ';';

read_s			:	'READ' '(' id_list ')' {ExprStack.evaluateRead($id_list.text, SymbolHashStack.checkType($id_list.text));}';';

write_s			:	'WRITE' '(' id_list ')' {ExprStack.evaluateWrite($id_list.text, SymbolHashStack.checkType($id_list.text));}';';

return_s		:	'RETURN' expression {ExprStack.functionReturn($expression.text);}';';


// Complex Statements

if_stmt			:	if_stmt_head if_stmt_body if_stmt_foot;

// If Statement Format

if_stmt_head	:	'IF' '(' cond ')' {SymbolHashStack.newBlock(); ExprStack.evaluateIf($cond.text);};

if_stmt_body	:	decl statement_list else_if;

if_stmt_foot	:	'ENDIF' {ExprStack.labelEndIf(); SymbolHashStack.popHash();};

else_if			:	( {SymbolHashStack.newBlock();} 'ELSIF' '(' cond ')' {ExprStack.evaluateElseIf($cond.text);} decl {SymbolHashStack.popHash();} statement_list else_if )?;

else_s			:	{SymbolHashStack.newBlock();} 'ELSE' decl {SymbolHashStack.popHash();} statement_list;

// Conditionals

cond			:	( expression compop expression ) | 'TRUE' | 'FALSE';

compop			:	'<' | '>' | '=' | '!=' | '<=' | '>=';

do_while_stmt	:	do_while_head do_while_body do_while_foot;

// Do While Statment Format

do_while_head	:	'DO' {SymbolHashStack.newBlock(); ExprStack.labelDoWhile();};

do_while_body	:	decl statement_list 'WHILE' '(' cond ')' {ExprStack.evaluateDoWhile($cond.text);};

do_while_foot	:	';' {SymbolHashStack.popHash();};






/* Expressions */

expression		:	factor expr_tail;	

factor			:	postfix_expr factor_tail;

expr_tail		:	(addop factor {ExprStack.addOperator($addop.text);} expr_tail)?;	

postfix_expr	:	primary | call_expr;

factor_tail		:	(mulop postfix_expr {ExprStack.addOperator($mulop.text);} factor_tail)?;

primary			:	('('expression')') | (single_id {ExprStack.addRIdentifier($single_id.text);});

single_id		:	id | INTLITERAL | FLOATLITERAL;

call_expr		:	id '(' expr_list? ')' {ExprStack.functionCall($id.text);};

expr_list		:	expression expr_list_tail {ExprStack.functionPush($expression.text);};

expr_list_tail	:	(',' expression expr_list_tail {ExprStack.functionPush($expression.text);})?;

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
