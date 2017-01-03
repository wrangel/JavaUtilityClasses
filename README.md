# JavaUtilityClasses

A collection of small classes that can be used generically when automating data wrangling or creating statistics.


The class "GeometricExpressionDecomposition" is a bit different. It celebrates the geometric progression:

Every positive integer can be expressed as the sum of unique members of the geometric progression (2^n, where n >= 0).

Examples: 

- 1 = 2^0
- 5 = 2^2 + 2^0 = 4 + 1
- 7 = 2^2 + 2^1 + 2^0 = 4 + 2 + 1

The largest exponent n to formulate an integer x is found as follows

2^n = (x+1)/2 <=> 
n	= ln((x+1)/2) / ln(2)

E.g. for 7, we have

n	= ln((7+1)/2) / ln(2) = 2
s.t. the largest element of the geometric expression is 2^2 = 4.

Creating a set (or, a list containing unique elements) of integers to formulate any other integer can be very useful. 

We had a set of customer attributes which all either could apply (1) or not apply (0). The number of attributes was extensible, and they all could apply simultaneously to the customer. This all had to be fed through an interface, which was not meant to change every time a new attribute was introduced. We created a single parameter for the interface, feeding in an integer value. The receiver decomposed the integer value into unique elements of the geometric expression.

This small class takes as input a member of the geometric expression 2^n, and calculates the decomposition of every integer on the interval [1, 2^n * 2 - 1]. 
