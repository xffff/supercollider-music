/************************************	*/
/* Ensemble Piece			       	*/
/*	SYNTHS						*/
/************************************	*/

~load_synths = fork{
s.waitForBoot(
SynthDef(\dryaudio, { | in = #[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15], 
						amp = 1, out = 1, dur = inf, free_trig = 0 |
	var sound, env;
	env = FreeSelf.kr(free_trig);
	sound = SoundIn.ar(in); 
	Out.ar(out, sound);
}).add;

SynthDef(\fxout, { | in = 0, amp = 1, out = 1, dur = inf, free_trig = 0 |
	var sound, env;
	env = FreeSelf.kr(free_trig);
	sound = In.ar(in); 
	Out.ar(out, sound);
}).add;

///////// synths /////////

SynthDef(\pulse, { | out = 0, amp = 0, atk = 0, sus = 0, rel = 0
					freq = 0, portamento = 0 |
	var env = EnvGen.kr(Env.linen(atk,sus,rel,amp),doneAction:2);
	var sound = Pulse.ar(Lag.kr(freq,portamento),0.5)*env;
	Out.ar(out, sound)
}).add;

SynthDef(\saw, { | out = 0, amp = 0, atk = 0, sus = 0, rel = 0
					freq = 0, portamento = 0 |
	var numvoices = 16;
	var env = EnvGen.kr(Env.linen(atk,sus,rel,amp,\sin),doneAction:2);
	var sound = Mix.fill(numvoices, { | i |
		i=i+1; 
//		RLPF.ar(
//			Saw.ar((freq*i),
//				SinOsc.kr(Rand(0.001,0.1),0,0.5,0.5)*1/numvoices
//			), 
//			SinOsc.kr(1/32).range(freq*1.25,6e3),
//			LFNoise2.kr(Rand(0.001,0.1)).range(0.2,0.7)
//		)*AmpComp.kr(freq*i,freq)
			Saw.ar((freq*i),
				SinOsc.kr(Rand(0.001,0.1),0,0.5,0.5)*1/numvoices
			)*AmpComp.kr(freq*i,freq)

	});
	sound = RLPF.ar(sound, SinOsc.kr(1/32).range(freq*1.1,6e3), 
		LFNoise2.kr(Rand(0.001,0.1)).range(0.2,0.7));
	sound = Limiter.ar(sound,0.8,0.02);
	Out.ar(out, sound*env)
}).add;

// hala
SynthDef(\hala, { | in = 0, out = 0, amp = 1.0, atk = 0.1, sus = 0.1, rel = 0.1, pan = 0 |
	var sound;
	sound = In.ar(in,1);
	sound = PanAz.ar(~numchans, sound, pan)*EnvGen.kr(Env.linen(atk,sus,rel,amp),doneAction:2);
	Out.ar(out,sound);
}).add;

////////// filters /////////

SynthDef(\bpf, { | in = 0, out = 0, amp = 1.0, atk = 0.1, sus = 10, rel = 0.1, 
						freq = 100, res = 0.7 | 
	var sound, env;
	sound = In.ar(in,1);
	env = EnvGen.kr(Env.linen(atk,sus,rel,amp),doneAction:2);
	sound = BPF.ar(sound,freq,res) * env;
	ReplaceOut.ar(out,sound);
}).add;

SynthDef(\rlpf, { | in = 0, out = 0, amp = 1.0, atk = 0.1, sus = 10, rel = 0.1, 
					startfreq = 100, endfreq = 100, res 0.2, time = 10 |
	var sound, env;
	sound = In.ar(in,1);
	env = EnvGen.kr(Env.linen(atk,sus,rel,amp),doneAction:2);
	sound = RLPF.ar(sound,Line.kr(startfreq,endfreq,time),res) * env;
	ReplaceOut.ar(out,sound);
}).add;

////////// fx /////////

SynthDef(\recbuf, { | in = 0, dur = 5, bufnum = 0 |
	Line.kr(1,0,dur,doneAction:2);
	RecordBuf.ar(In.ar(in),bufnum,0,doneAction:2,loop:0);
}).add;

SynthDef(\playbuf, { | out = 0, bufnum = 0, rate = 1, 
						atk = 0.1, sus = 0.1, rel = 0.1, 
						amp = 0, loop = 0, startpos = 0 |
	var env, sound;
	env = EnvGen.kr(Env.linen(atk,sus,rel,amp), doneAction:2);
	sound = PlayBuf.ar(1,bufnum,
		BufRateScale.kr(bufnum)*rate, loop: loop, startPos: startpos) * env;
	Out.ar(out, sound);
}).add;

SynthDef(\magfreeze, { | in = 0, out = 0, amp = 1, atk = 0.1, sus = 10, rel = 0.1, trig = 0 | 
	var sound, env, chain;
	env = EnvGen.kr(Env.linen(atk,sus,rel,amp,'sin'),doneAction:2);
	sound = In.ar(in, 1);
	chain = FFT(LocalBuf(4096), sound);
	chain = PV_MagFreeze(chain, trig);
	Out.ar(out, env * IFFT(chain));
}).add;

SynthDef(\gverb, { | in = 0, out = 0, amp = 1, atk = 0.1, sus = 10, rel = 0.1,
					roomsize = 10, revtime = 3, damping = 0.5, inputbw = 0.5, spread = 15, 
					drylevel, earlylevel, taillevel | 
	var sound, env;
	env = EnvGen.kr(Env.linen(atk,sus,rel,amp,'sin'),doneAction:2);
	sound = In.ar(in, 1);

// USE THE LEXICON PCM FOR NOW... MAYBE SOON TO BE H8000 :D
//	sound = GVerb.ar(
//� � � � � � � � sound,
//� � � � � � � � roomsize,
//� � � � � � � � revtime,
//� � � � � � � � damping,
//� � � � � � � � inputbw,
//� � � � � � � � spread,
//� � � � � � � � drylevel.dbamp,
//� � � � � � � � earlylevel.dbamp,
//� � � � � � � � taillevel.dbamp,
//� � � � � � � � roomsize).sum;

	Out.ar(out, sound*env);
}).add;

SynthDef(\crusher, { | in = 0, out = 0, amp = 1, atk = 0.1, sus = 10, rel = 0.1, 
						bitdepth = 32, gate = 1, lag = 2 |
		var env, signal;
		env = EnvGen.kr(Env.linen(atk,sus,rel,amp,'sin'),doneAction:2);
		signal = In.ar(in, 1).trunc(0.5**Lag.kr(bitdepth,lag)) * env;
		signal = LPF.ar(signal, 1500);
		Out.ar(out, signal);
}).add;

SynthDef(\pitchshift, { | in = 0, out = 0, ratio = 0, amp = 1, atk = 0.1, sus = 10, rel = 0.1,
						windowSize = 0.2, timeDispersion = 0.0001, pitchDispersion = 0.0001,
						bpfselect = 0, bpffreq = 500, bpfres = 0.5 |
	var sound, env;
	env = EnvGen.kr(Env.linen(atk,sus,rel,amp,'sin'),doneAction:2);
	sound = In.ar(in, 1);
	sound = PitchShift.ar(sound, windowSize, ratio, 
		timeDispersion, pitchDispersion) * env;
	sound = Select.ar(bpfselect, [sound, BPF.ar(sound,bpffreq,bpfres)]);
	Out.ar(out,sound);
}).add;

SynthDef(\freqshift, { | in = 0, out = 0, amp = 1, atk = 0.1, sus = 10, rel = 0.1,
						freq = 0 |
	var sound, env;
	env = EnvGen.kr(Env.linen(atk,sus,rel,amp,'sin'),doneAction:2);
	sound = In.ar(in, 1);
	sound = FreqShift.ar(sound, freq) * env;
	4.do{sound = DelayC.ar(sound,0.4,0.4.rand,0.25,sound)};
	Out.ar(out,sound);
}).add;

SynthDef(\fpres_pitchshift, { |in = 0, out = 0, atk, sus, rel, 
							itransp = 0, transp = 0, ftransp = 0,
              				amp = 0.1, pwidth = 0.25, pvar = 0, tvar = 0,
              				gate = 1, hishelf = (-3), lowCut = 20|

	var sig, modSig, freq, haspitch, ratio, fratio;
	var a, b, c;
	var chain1, chain2;
	var env;
	       
	env = EnvGen.kr(Env.linen(atk,sus,rel,amp),doneAction:2);
	       
	ratio = (itransp + transp).lag(0.5).midiratio;
	fratio = ftransp.lag(0.5).midiratio;      
	#a,b = { LocalBuf(1024).clear; }!2;
	sig = In.ar(in,1);
	 #freq, haspitch = Tartini.kr( sig, n:4096 );
	       
	// generate new fundamental
	modSig = Impulse.ar( (freq.lag(0.25) * ratio / fratio ) * LFNoise2.kr(2,0.1).midiratio);
	       
	chain1 = FFT( a, sig );
	chain2 = FFT( b, modSig );
	chain2 = PV_MagMul( chain2, chain1 );
	       
	sig = IFFT( chain2 );
	sig = PitchShift.ar( sig, 0.2, 1, pvar.midiratio - 1, tvar * 0.2 );
	sig = sig * Line.ar( -1,1,0.5).max(0);
	sig = BHiShelf.ar( sig, 1200, 1, hishelf );
	sig = BLowCut.ar( sig, lowCut.lag(1).clip(20,20000), 3 );
              
	Out.ar(out, sig * env);
}).add; 

SynthDef(\convolve, { | in = 0, convin = 1, amp = 0.1, out = 0, 
						atk = 0.1, sus = 0.1, rel = 0.1 | 
	var env, sound, kernel;
	env = EnvGen.kr(Env.linen(atk,sus,rel,amp),doneAction:2);
	kernel = In.ar(convin, 1);
	sound = Convolution.ar(In.ar(in,1),kernel,4096);
	Out.ar(out,sound*env);
}).add;

SynthDef(\distortion, { | in = 0, out = 0, amount = 0, amp = 0, atk = 10, 
						sus = 10, rel = 10, dur = 10 |
	var sound, k, env;
	env = EnvGen.kr(Env.linen(atk,sus,rel,amp),doneAction:2);
	sound = In.ar(in, 1);
	k = 2 * amount / (1-amount);
	sound = (1+k) * sound / (1+(k*sound.abs));
	sound = sound * env;
	Out.ar(out,sound);
}).add;

SynthDef(\fbdelay,{ | in = 0, out = 0, atk = 0.1, sus = 0.1, rel = 0.1, 
						delay = 10, maxdelay = 10, amp = 1.0, fb = 0.1, 
						filterselect = 0, filterfreq = 100 |
	var env, sound;
	env = EnvGen.kr(Env.linen(atk,sus,rel,amp),doneAction:2);	sound = In.ar(in,1);
	sound = sound + LocalIn.ar(1);
	sound = DelayC.ar(sound, maxdelay, delay - ControlDur.ir).tanh;
	sound = Select.ar(filterselect, [sound, LPF.ar(sound,filterfreq)]);
	LocalOut.ar(sound)*fb;
	Out.ar(out,sound*env);
}).add;

SynthDef(\grain, { | bufnum = 0, atk = 0, sus = 0, rel = 0, center = 0,
					amp = 0, out = 0, ratelow = 1, ratehigh = 1, 
					graindur = 1, grainfreq = 1 |
	var trig = Dust.kr(grainfreq);
	var env = EnvGen.kr(Env.linen(atk,sus,rel,amp,\sin), doneAction:2);
	var sound = TGrains.ar(2, trig, bufnum, rate: LFNoise0.kr(grainfreq).range(ratelow, ratehigh), 
		centerPos: center, dur: graindur, amp: 1.0).sum; // mono!
	Out.ar(out, sound*env);
}).add;

SynthDef(\freeze, { | bufnum = 0, out = 0, rate = 1, bufdur = 2,
					atk = 0, sus = 0, rel = 0, amp = 0.1, cfreq = 0.1, 
					cphase = 0, cmul = 1, cadd = 1 | 
	var env = EnvGen.kr(Env.linen(atk,sus,rel,amp), doneAction:2);
	var tfreq = 100;
	var trig = Impulse.kr(tfreq);
	var center = LFSaw.kr(cfreq,cphase,cmul,cadd);
	var graindur = 10 / tfreq;
	var sound = TGrains.ar(2, trig, bufnum, rate: rate, 
		centerPos: center, dur: graindur, amp: 1.0).sum; // mono!
	Out.ar(out, sound*env);
}).add;

SynthDef(\warp, { | in = 0, out = 0, amp = 0, dur = 10, sus = 0, atk = 0.01, rel = 0.01, 
					hpfreq = 20, warpfactor = 1, freqscale = 1, windowsize = 0.2, 
					overlaps = 10, windowrandratio = 0.1, prelevel = 0,
					interp = 4 |
	var sound, env, buf;
	buf = LocalBuf(SampleRate.ir*dur,1).clear;
	env = EnvGen.kr(Env.linen(atk,sus,rel,amp,'sin'), doneAction:2);
	sound = In.ar(in, 1);
	sound = WarpIn.ar(sound,buf,warpfactor,freqscale,windowsize,-1,
			overlaps,windowrandratio,prelevel,interp);
	sound = HPF.ar(sound,hpfreq);
	sound = sound * env;
	Out.ar(out, sound.tanh);
}).add;

s.sync;

postln("Synths loaded");
)
};
////////////////////////////////////////////////////
