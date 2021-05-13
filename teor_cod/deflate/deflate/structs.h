#ifndef __STRUCTS_H__
#define __STRUCTS_H__

typedef struct sub_of_Haf_st
{
	int count;
	int *sym_Haf;
}sym;


typedef struct Tree_st
{
	int count; 
	unsigned char val;
	struct Tree_st *right;
	struct Tree_st *left;
	struct Tree_st *root;
} tree;

typedef struct Priority_Q_st
{
	tree *elem;
	struct Priority_Q_st *next;
}prio_q;

#endif