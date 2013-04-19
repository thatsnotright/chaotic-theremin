__author__ = 'rob'

def logistic_map(x0, r, n):
    if n == 0: return x0

    xprev = logistic_map(x0, r, n - 1)
    return r * xprev * (1 - xprev)

