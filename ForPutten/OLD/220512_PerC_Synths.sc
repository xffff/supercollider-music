/************************************	*/
/* Percussion Piece		       	*/
/*	SYNTHS						*/
/************************************	*/

~load_synths = fork{
s.waitForBoot(
SynthDef(\dryaudio, { | in = #[0,1,2,3,4,5,6,7], amp = 1, out = 1, dur = inf, free_trig = 0 |
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

SynthDef(\quadc, { | out = 0, freq = 48000, amp = 0.1, atk = 0.01, dec = 0.5 |
	var r,s;
	r = LFNoise2.ar(16).range(3.5441, 4); Ê Ê Ê 
	s = QuadC.ar(freq, r.neg, r, 0, 0.1) * EnvGen.ar(Env.perc(atk,dec),doneAction:2);
	s = s * amp;
	s = Limiter.ar(s,1.0,0.01);
Out.ar(out,s);
}).add;

SynthDef(\sin_exprand16, { | out = 0, bfreq = 500, tfreq = 1000, amp = 0.1, atk = 0.01, dec = 0.5 |
	var sound;
	sound = Mix.fill(16, {SinOsc.ar(ExpRand(bfreq,tfreq), 0, 1/16)}); 
	sound = sound * EnvGen.ar(Env.perc(atk,dec,amp),doneAction:2); 
	Out.ar(out,sound);
}).add;

SynthDef(\saw_perc, { | out = 0, freq = 60, amp = 0, filterfreq = 80, res = 0.2, dur = 0.5 |
	var sound;
	sound = Saw.ar([freq*0.99,freq, freq*1.01], 1/3).sum;
	sound = sound * EnvGen.ar(Env.perc(0.01,dur,amp),doneAction:2);
	sound = RLPF.ar(sound, filterfreq.max(freq+1), res);
	Out.ar(out,sound);
}).add;

SynthDef(\saw_linen, { | out = 0, atk = 0.1, sus = 10, rel = 0.1, 
						freq = 60, amp = 0, filterfreq = 80, res = 0.2, dur = 0.5 |
	var sound;
	sound = Saw.ar([freq*0.99,freq, freq*1.01], 1/3).sum;
	sound = sound * EnvGen.ar(Env.linen(atk,sus,rel,amp,'sin'),doneAction:2);
	sound = RLPF.ar(sound, filterfreq.max(freq+1), res);
	Out.ar(out,sound);
}).add;

SynthDef(\subsyn, { | out = 0, atk = 0.1, sus = 10, rel = 0.1,  bw = 0.001,
						freq = 60, amp = 0, modmul = 0.01, modfreq = 0.1,
						amselect = 0, amfreq = 1.0, amenvrel = 1.0, decay = 1.0
						amfreqline = 0, amfreqend = 1.0, amfreqlinetime = 10,
						filterselect = 0, filterfreqstart = 100, filterfreqend = 100,
						filterlinetime = 10 |
	var sound, amenv, amtrig, mod;
	mod = SinOsc.kr(modfreq,0,modmul);
	amtrig = Select.kr(amfreqline, [Impulse.kr(amfreq.reciprocal), 
		Impulse.kr(Line.kr(amfreq.reciprocal,amfreqend.reciprocal,amfreqlinetime))]);
	amenv = EnvGen.ar(Env.perc(0.001,amenvrel), amtrig);
	sound = Resonz.ar(WhiteNoise.ar(amp),mod+freq,bw); // might have to make another mod
	sound = sound * EnvGen.ar(Env.linen(atk,sus,rel,amp,'sin'),doneAction:2);
	sound = Select.ar(amselect, [sound, sound * amenv]);
	sound = Select.ar(filterselect, [sound, LPF.ar(sound,Line.kr(filterfreqstart,filterfreqend,filterlinetime)+mod)]);
	10.do{AllpassC.ar(sound,0.1,0.1.rand,decay,1.0,sound)};
	Out.ar(out,sound.tanh);
}).add;

////////// fx /////////

SynthDef(\recbuf, { | in = 0, dur = 5, bufnum = 0 |
	Line.kr(1,0,dur,doneAction:2);
	RecordBuf.ar(In.ar(in),bufnum,0,doneAction:2,loop:0);
}).add;

SynthDef(\playbuf, { | out = 0, bufnum = 0, rate = 1, atk = 0.1, sus = 0.1, rel = 0.1, amp = 0, loop = 0 |
	var env, sound;
	env = EnvGen.ar(Env.linen(atk,sus,rel,amp), doneAction:2);
	sound = PlayBuf.ar(1,bufnum,BufRateScale.kr(bufnum)*rate, loop: loop) * env;
	Out.ar(out, sound);
}).add;

SynthDef(\pitchshift, { | in = 0, out = 0, ratio = 0, amp = 1, atk = 0.1, sus = 10, rel = 0.1,
						windowSize = 0.2, timeDispersion = 0.0001, pitchDispersion = 0.0001,
						bpfselect = 0, bpffreq = 500, bpfres = 0.5 |
	var sound, env;
	env = EnvGen.ar(Env.linen(atk,sus,rel,amp,'sin'),doneAction:2);
	sound = In.ar(in, 1);
	sound = PitchShift.ar(sound, windowSize, ratio, 
		timeDispersion, pitchDispersion) * env;
	sound = Select.ar(bpfselect, [sound, BPF.ar(sound,bpffreq,bpfres)]);
	Out.ar(out,sound);
}).add;

SynthDef(\tri_pitchshift, { | in = 0, out = 0, ratio = 0, amp = 1, atk = 0.1, sus = 10, rel = 0.1,  
						windowSize = 0.2, timeDispersion = 0.0001, pitchDispersion = 0.0001 |
	var sound, env;
	env = EnvGen.ar(Env.linen(atk,sus,rel,amp),doneAction:2);
	sound = In.ar(in, 3);
	sound = sound.sum;
	sound = PitchShift.ar(sound, windowSize, ratio, 
		timeDispersion, pitchDispersion);
	Out.ar(out,sound*env);
}).add;

SynthDef(\quad_pitchshift, { | in = 0, out = 0, ratio = 0, amp = 1, atk = 0.1, sus = 10, rel = 0.1, 
						windowSize = 0.2, timeDispersion = 0.0001, pitchDispersion = 0.0001 |
	var sound, env;
	env = EnvGen.ar(Env.linen(atk,sus,rel,amp),doneAction:2);
	sound = In.ar(in, 4);
	sound = sound.sum;
	sound = PitchShift.ar(sound, windowSize, ratio, 
		timeDispersion, pitchDispersion);
	Out.ar(out,sound*env);
}).add;

SynthDef(\freqshift, { | in = 0, out = 0, ratio = 0, amp = 1, atk = 0.1, sus = 10, rel = 0.1,
						freq = 0, lagtime = 0 |
	var sound, env;
	env = EnvGen.ar(Env.linen(atk,sus,rel,amp),doneAction:2);
	sound = In.ar(in, 1);
	sound = FreqShift.ar(sound, Line.kr(freq,freq,lagtime)) * env;
	Out.ar(out,sound);
}).add;

SynthDef(\tri_freqshift, { | in = 0, out = 0, ratio = 0, amp = 1, atk = 0.1, sus = 10, rel = 0.1,
						freq = 0, lagtime = 0, decaytime = 1.0, rvbselect = 0,  
						filterswitch = 0, cutoff = 500, res = 0.2 |
	var sound, env, filterenv;
	env = EnvGen.ar(Env.linen(atk,sus,rel,amp),doneAction:2);
	filterenv = EnvGen.ar(Env.new([10e-10, 1, 10e-10], [0.01, rel], 'exp'), 0.5);
	sound = In.ar(in, 3);
	sound = sound.sum;
	sound = FreqShift.ar(sound, Line.kr(freq,freq,lagtime)) * env;
	sound = Select.ar(filterswitch, [sound, RLPF.ar(sound, cutoff+(filterenv*env), res)] );
	10.do{AllpassC.ar(sound,0.1,0.1.rand,decaytime,1.0,sound)};
	Out.ar(out,sound);
}).add;

SynthDef(\convolve, { | in = 0, out = 0.1, atk = 0.1, sus = 0.1, rel = 0.1, 
						amp = 0, freq = 100, synthmul = 1.0, filterfreq = 100, res = 0.2 | 
	var env, sound, kernel;
	env = EnvGen.ar(Env.linen(atk,sus,rel,amp),doneAction:2);
	kernel = RLPF.ar(LFSaw.ar(freq,0,synthmul),filterfreq.max(freq),res);
	sound = Convolution.ar(In.ar(in,1),kernel,4096);
	sound = Limiter.ar(sound, 0.95, 0.01); // for some reason this convolution ends up very loud occasionally
	Out.ar(out,sound*env);
}).add;

SynthDef(\distortion, { | in = 0, out = 0, amount = 0, amp = 0, atk = 10, 
						sus = 10, rel = 10, dur = 10 |
	var sound, k, env;
	env = EnvGen.ar(Env.linen(atk,sus,rel,amp),doneAction:2);
	sound = In.ar(in, 1);
	k = 2 * amount / (1-amount);
	sound = (1+k) * sound / (1+(k*sound.abs));
	sound = sound * env;
	Out.ar(out,sound);
}).add;

SynthDef(\bpf, { | in = 0, out = 0, amp = 1.0, atk = 0.1, sus = 10, rel = 0.1, 
						freq = 100, res = 0.7 | 
	var sound, env;
	sound = In.ar(in,1);
	env = EnvGen.ar(Env.linen(atk,sus,rel,amp),doneAction:2);
	sound = BPF.ar(sound,freq,res) * env;
	Out.ar(out,sound);
}).add;

SynthDef(\mod_delay, { | in = 0, out = 0, freq = 0.25, amp = 0, mul = 0, 
						add = 0, maxdelay = 5, dur = 10 |
	var sound, env;
	sound = In.ar(in, 1);
	env = EnvGen.ar(Env.linen(0.01,dur,0.01,amp), doneAction:2);
	sound = DelayC.ar(sound, maxdelay, LFSaw.ar(freq, 0, mul, add).abs, env); // +=
	sound = LeakDC.ar(sound,0.995);
	Out.ar(out, sound);
}).add;

SynthDef(\fbdelay,{ | in = 0, out = 0, atk = 0.1, sus = 0.1, rel = 0.1, 
						delay = 10, maxdelay = 10, amp = 1.0, fb = 0.1, 
						filterselect = 0, filterfreq = 100 |
	var env, sound;
	env = EnvGen.ar(Env.linen(atk,sus,rel,amp),doneAction:2);	sound = In.ar(in,1);
	sound = sound + LocalIn.ar(1);
	sound = DelayC.ar(sound, maxdelay, delay - ControlDur.ir).tanh;
	sound = Select.ar(filterselect, [sound, LPF.ar(sound,filterfreq)]);
	LocalOut.ar(sound)*fb;
	Out.ar(out,sound*env);
}).add;

SynthDef(\duo_fbdelay,{ | in = 0, out = 0, atk = 0.1, sus = 0.1, rel = 0.1, 
						delay = 10, maxdelay = 10, amp = 1.0, fb = 0.1 |
	var env, sound;
	env = EnvGen.ar(Env.linen(atk,sus,rel,amp),doneAction:2);	sound = In.ar(in,2);
	sound = Mix.fill(2,{|i| sound[i]});
	sound = sound + LocalIn.ar(1);
	sound = DelayC.ar(sound, maxdelay, delay - ControlDur.ir);
	LocalOut.ar(sound)*fb;
	Out.ar(out,sound*env);
}).add;

//SynthDef(\grainbuf, { | bufnum = 0, out = 0, rate = 1, freq = 10, atk = 0.1, sus = 0.1, rel = 0.1 | 
//	
//}).add;

SynthDef(\grainer, { | in = 0, out = 0, freq = 0.1, pan = 0.0, rel = 0.1, hpfreq = 500,
						graindur = 1, rate = 1, amp = 0, dur = 10 |
	var buf, sound, rec, trigger, env;
	env = EnvGen.ar(Env.linen(0.01,dur,rel), doneAction:2);
	buf = LocalBuf(SampleRate.ir,1).clear;
	sound = In.ar(in, 1);
	sound = HPF.ar(sound,hpfreq);
	rec = RecordBuf.ar(sound, buf, loop:1);
	sound = PlayBuf.ar(1, buf, 1, loop:1);
	trigger = Dust.ar(freq);
	sound = GrainBuf.ar(1, trigger, graindur, buf, 
		LFNoise2.kr(freq).range(0.5,2.0), SinOsc.kr(1).range(0,1), 4, pan);
	sound = LeakDC.ar(sound, 0.995);
	sound = sound * env * amp;
	Out.ar(out, sound)
}).add;

SynthDef(\delay_grainer, { | in = 0, out = 0, amp = 0, dur = 10, rel = 0.1 |
	var sound, env, freqs, numvoices, maxdelaytime;
	numvoices = 32;
	maxdelaytime = 5.0;
	env = EnvGen.ar(Env.linen(0.01,dur,rel), doneAction:2);
	sound = In.ar(in, 1);
	sound = Mix.fill(numvoices, { |i|
			var freq,muladd;
			muladd = Rand(0.0, maxdelaytime * 0.5); 
			freq = LFSaw.kr(Rand(0.1,1.0),0,Rand(0.0,muladd),muladd);
			//freq = Rand(0.1,maxdelaytime);
			DelayC.ar(sound, maxdelaytime, freq.abs)
		});
	//sound = HPF.ar(sound, 20);
	sound = sound * env * amp;
	Out.ar(out, sound);	
}).add;

SynthDef(\warpin, { | in = 0, out = 0, amp = 0, dur = 10, sus = 0, atk = 0.01, rel = 0.01, 
					hpfreq = 20, warpfactor = 1, freqscale = 1, windowsize = 0.2, 
					overlaps = 10, windowrandratio = 0.1, prelevel = 0,
					interp = 4 |
	var sound, env, buf;
	buf = LocalBuf(SampleRate.ir*dur,1).clear;
	env = EnvGen.ar(Env.linen(atk,sus,rel,amp,'sin'), doneAction:2);
	sound = In.ar(in, 1);
	sound = WarpIn.ar(sound,buf,warpfactor,freqscale,windowsize,-1,
			overlaps,windowrandratio,prelevel,interp);
	sound = HPF.ar(sound,hpfreq);
	sound = sound * env;
	Out.ar(out, sound.tanh);
}).add;

SynthDef(\quad_warpin, { | in = 0, out = 0, amp = 0, dur = 10, sus = 0, atk = 0.01, rel = 0.01, 
					hpfreq = 20, warpfactor = 1, freqscale = 1, windowsize = 0.2, 
					overlaps = 10, windowrandratio = 0.1, prelevel = 0,
					interp = 4 |
	var sound, env, buf;
	buf = LocalBuf(SampleRate.ir*dur,1).clear;
	env = EnvGen.ar(Env.linen(atk,sus,rel,amp,'sin'), doneAction:2);
	sound = In.ar(in, 4);
	sound = Mix.fill(4, { |i| WarpIn.ar(sound[i],buf,warpfactor,freqscale,windowsize,-1,
			overlaps,windowrandratio,prelevel,interp) });
	sound = HPF.ar(sound,hpfreq);
	sound = sound * env;
	Out.ar(out, sound);
}).add;

s.sync;

postln("Synths loaded");
)
};
////////////////////////////////////////////////////
