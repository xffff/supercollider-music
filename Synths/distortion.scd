k = { arg in = 1, amount = 0.99, f1 = 3800, f2 = 3900, vol = 0.15 ;
var sig, amCoef;
sig = HPF.ar(SoundIn.ar(in), 400)*5;
amCoef= 2*amount/(1-amount);
MidEQ.ar(LPF.ar((1+amCoef)*sig/(1+(amCoef*sig.abs)), [f1, f2])*0.5, 120, 0.7, 8)*vol;
}.play