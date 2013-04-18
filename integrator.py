#! /usr/bin/python
__author__ = 'rob'

import csv # for the boring ):
import numpy # for the awesome!
import sys

x=float(sys.argv[1])
y=float(sys.argv[2])
z=float(sys.argv[3])
timestep=float(sys.argv[4])
runtime=float(sys.argv[5])
fname=sys.argv[6]

x0 = numpy.array([x,y,z])

def integrate(tn, yn, dt, f):
    k1=dt*f(dt, tn,        yn)
    k2=dt*f(dt, tn+dt/2.0, yn+k1/2.0)
    k3=dt*f(dt, tn+dt/2.0, yn+k2/2.0)
    k4=dt*f(dt, tn+dt,     yn+k3)
    yp = yn+(1.0/6.0)*(k1+2.0*k2+2.0*k3+k4)
    return yp

def run_sim(t0, t_max, x0, delta_t, f, filename):
    try:
        with open(filename, 'w') as file:
            x = x0
            t = t0
            writer = csv.writer(file, delimiter=',',
                quotechar='"', quoting=csv.QUOTE_MINIMAL)
            while t < t_max:
                b = [round(t,3)]
                [b.append(round(z,3)) for z in x]
                writer.writerow(b)
                ox = [x[3:6],x[6:9],x[9:12]] 
                x = integrate(t, x, delta_t, f)
                t = t + delta_t

    except IOError as e:
        print 'Operation failed: %s' % e.strerror

def yp(dt, t, ip):
    a=16
    r=45
    b=4
    x   = ip[0]
    y   = ip[1]
    z   = ip[2]
    return numpy.array([
        a*(y-x),
        r*x-y-x*z,
        x*y-b*z
    ])

print fname
run_sim(0.0, runtime, x0, timestep, yp, fname)
