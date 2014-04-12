/************************************	*/
/* Untitled Piece			         	*/
/*	SYNTHS					        	*/
/************************************	*/

~load_synths = fork{
	s.waitForBoot({
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

		SynthDef(\mixerchannel, { |in = 0, out = 0, db = -100, dur = inf, free_trig = 0, pan = 0, numchans = 1 |
			var sound, env;
			env = FreeSelf.kr(free_trig);
			sound = In.ar(in,1);
			sound = sound * db.dbamp;
			sound = PanAz.ar(~numoutchans,sound,pan);
			Out.ar(out, sound);
		}).add;

		SynthDef(\dawmixerchannel, { |in = 0, out = 0, db = -100, dur = inf, free_trig = 0, pan = 0, numchans = 1 |
			var sound, env;
			env = FreeSelf.kr(free_trig);
			sound = In.ar(in,1);
			sound = sound * db.dbamp;
			Out.ar(out, sound);
		}).add;

		// hala
		SynthDef(\hala, { | in = 0, out = 0, amp = 1.0, atk = 0.1, sus = 0.1, rel = 0.1, pan = 0 |
			var sound;
			sound = In.ar(in,1);
			sound = PanAz.ar(~numoutchans, sound, pan)*EnvGen.kr(Env.linen(atk,sus,rel,amp),doneAction:2);
			Out.ar(out,sound);
		}).add;

		///////// synths /////////


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
	})
};
////////////////////////////////////////////////////
