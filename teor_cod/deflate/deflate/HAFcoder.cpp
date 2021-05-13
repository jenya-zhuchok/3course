#include "haf.hpp"
#include <iostream>

sym* Haf[256];
int abc[256] = { 0 };
int haf_cod[8] = { 0 };

int num_bit = 0;
int arr_bit[8] = { 0 };

void gather_statistic(std::string str) {
	for (int i = 0; i < str.length(); i++)
		abc[(unsigned char)str.at(i)]++;
}

tree* create_branch(int* abc, int num)
{
	tree* root = (tree*)calloc(1, sizeof(tree));
	memset(root, 0, sizeof(tree));
	root->count = abc[num];
	root->val = (unsigned char)num;
	return root;
}

prio_q* build_queue(int num, prio_q* head)
{
	if (!head)
	{
		prio_q* new_el = (prio_q*)calloc(1, sizeof(prio_q));
		new_el->elem = create_branch(abc, num);
		head = new_el;
		return head;
	}
	if (head->elem->count < abc[num])
	{
		head->next = build_queue(num, head->next);
		return head;
	}

	prio_q* new_el = (prio_q*)calloc(1, sizeof(prio_q));
	new_el->elem = create_branch(abc, num);

	new_el->next = head;
	head = new_el;
	return head;
}

tree* pop(prio_q** el)
{
	prio_q* tmp = *el;
	tree* tr = (*el)->elem;

	*el = (*el)->next;
	free(tmp);

	return tr;
}

tree* merge(tree* l, tree* r)
{
	tree* new_branch = (tree*)calloc(1, sizeof(tree));
	new_branch->count = l->count + r->count;
	new_branch->left = l;
	new_branch->right = r;
	return new_branch;
}

prio_q* push(prio_q* head, tree* el)
{
	if (!head)
	{
		prio_q* new_el = (prio_q*)calloc(1, sizeof(prio_q));
		new_el->elem = el;
		head = new_el;
		return head;
	}
	if (head->elem->count < el->count)
	{
		head->next = push(head->next, el);
		return head;
	}

	prio_q* new_el = (prio_q*)calloc(1, sizeof(prio_q));
	new_el->elem = el;

	new_el->next = head;
	head = new_el;
	return head;
}

tree* build_tree(prio_q* head)
{
	if (!head) return NULL;
	tree* a, * b, *c;
	while ((head) && (head->next))
	{
		a = pop(&head);
		b = pop(&head);

		c = merge(a, b);
		head = push(head, c);
	}

	a = pop(&head);
	return a;
}

unsigned char make_byte(int num){
	arr_bit[num_bit] = num;
	num_bit++;
	
	unsigned char c = NULL;
	if (num_bit == 8)
	{
		for (int j = 0; j < 8; j++)
			c = ((c << 1) | arr_bit[j]);
		num_bit = 0;
		std::cout << "ping\n";
	}
	return c;
}

void make_Haf_sym(unsigned char symb, int i){
	Haf[symb] = (sym*)calloc(1, sizeof(sym));
	Haf[symb]->count = i;
	Haf[symb]->sym_Haf = (int*)calloc(i, sizeof(int));
	for (int j = 0; j <= i; j++)
		Haf[symb]->sym_Haf[j] = haf_cod[j];
}

void dfs(tree* root, int count_haf){
	if (root->left){
		haf_cod[count_haf] = 0;
		dfs(root->left, count_haf + 1);
	}
	if (root->right){
		haf_cod[count_haf] = 1;
		dfs(root->right, count_haf + 1);
	}
	if (!((root->left) || (root->left))){
		unsigned int c;
		for (int j = 0; j < 8; j++)
			c = ((root->val) >> (7 - j)) & 1;
		make_Haf_sym(root->val, count_haf);
	}
}


std::string coding_text(std::string in){
	unsigned char c = NULL;

	std::string out = "";

	for (int i = 0; i < in.length(); i++){
		unsigned char s = (unsigned char)in.at(i);
		if (Haf[s] == NULL) continue;
		for (int j = 0; j < Haf[s]->count; j++) {
			c = make_byte(Haf[s]->sym_Haf[j]);
			if( c != NULL)
				out.push_back(c);
		}

	}

	if (c != NULL) return out;
	while (c == NULL)
		c = make_byte(0);
	out.push_back(c);
	return out;
}


std::string create_new_block(std::string in, tree* root, int flag)
{
	if (flag == 0) make_byte(0);
	else make_byte(1);

	make_byte(1);
	make_byte(0);

	dfs(root, 0);
	num_bit = 3; // шо происходит?

	return coding_text(in);
	
}

std::string hafencode(std::string input, int flag) {
	prio_q* head = NULL;
	tree* root;
	
	gather_statistic(input);

	for (int i = 0; i < 256; i++)
		if (abc[i] != 0)
			head = build_queue(i, head);

	root = build_tree(head);

	return create_new_block(input, root, flag);
}