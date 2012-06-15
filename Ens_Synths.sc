/************************************	*/
/* Ensemble Piece			       	*/
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


// hala

SynthDef(\hala, { | in = 0, out = 0, amp = 1.0, atk = 0.1, sus = 0.1, rel = 0.1, pan = 0 |
	var sound;
	sound = In.ar(in,1);
	sound = PanAz.ar(~numchans, sound, pan)*EnvGen.ar(Env.linen(atk,sus,rel,amp),doneAction:2);
	Out.ar(out,sound);
}).add;

////////// filters /////////

SynthDef(\bpf, { | in = 0, out = 0, amp = 1.0, atk = 0.1, sus = 10, rel = 0.1, 
						freq = 100, res = 0.7 | 
	var sound, env;
	sound = In.ar(in,1);
	env = EnvGen.ar(Env.linen(atk,sus,rel,amp),doneAction:2);
	sound = BPF.ar(sound,freq,res) * env;
	Out.ar(out,sound);
}).add;

SynthDef(\rlpf, { | in = 0, out = 0, amp = 1.0, atk = 0.1, sus = 10, rel = 0.1, 
					startfreq = 100, endfreq = 100, res 0.2, time = 10 |
	var sound, env;
	sound = In.ar(in,1);
	env = EnvGen.ar(Env.linen(atk,sus,rel,amp),doneAction:2);
	sound = RLPF.ar(sound,Line.kr(startfreq,endfreq,time),res) * env;
	Out.ar(out,sound);
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

SynthDef(\freqshift, { | in = 0, out = 0, ratio = 0, amp = 1, atk = 0.1, sus = 10, rel = 0.1,
						freq = 0, lagtime = 0 |
	var sound, env;
	env = EnvGen.ar(Env.linen(atk,sus,rel,amp),doneAction:2);
	sound = In.ar(in, 1);
	sound = FreqShift.ar(sound, Line.kr(freq,freq,lagtime)) * env;
	Out.ar(out,sound);
}).add;

SynthDef(\convolve, { | in = 0, out = 0.1, atk = 0.1, sus = 0.1, rel = 0.1, 
						amp = 0, freq = 100, synthmul = 1.0, filterfreq = 100, res = 0.2 | 
	var env, sound, kernel;
	env = EnvGen.ar(Env.linen(atk,sus,rel,amp),doneAction:2);
	kernel = RLPF.ar(LFSaw.ar(freq,0,synthmul),filterfreq.max(freq),res);
	sound = Convolution.ar(In.ar(in,1),kernel,4096);
	Out.ar(out,sound*env);
}).add;

// partconv
//SynthDef(\partconv, { | in = 0, out = 0, atk = 0.1, sus = 0.1, rel = 0.1, amp = 0,
//						bufnum = 0|
//	PartConv
//}).add;

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


//SynthDef(\grainer, { | bufnum = 0, out = 0, rate = 1, freq = 10, atk = 0.1, sus = 0.1, rel = 0.1 | 
//	
//}).add;

SynthDef(\freeze, { | bufnum = 0, out = 0, rate = 1, freq = 10, bufdur = 2,
					atk = 0, sus = 0, rel = 0, amp = 0.1, 
					levels = #[0.1, 0.5, 0.5, 0.9], times = #[1, 2, 1] | 
	var rdur = times[0]+times[1]+times[2];
	var env = EnvGen.ar(Env.linen(atk/rdur,sus/rdur,rel/rdur,amp), doneAction:2);
	var tfreq = 44;
	var trig = Impulse.kr(tfreq);
	var center = EnvGen.kr(
		Env(levels * bufdur, times, #[-5, 0, 5]),
		timeScale: bufdur, doneAction: 2
	);
	var graindur = 12 / tfreq;
	var sound = TGrains.ar(2, trig, bufnum, rate: 1, 
		centerPos: center, dur: graindur, amp: env).sum; // mono!
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

s.sync;

postln("Synths loaded");
)
};
////////////////////////////////////////////////////
