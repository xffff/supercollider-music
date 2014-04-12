

Help.gui;

(
o = Server.local.options;
o.numOutputBusChannels = 12;
o.numInputBusChannels = 16;
//o.inDevice="SF16 and MR816";
//o.outDevice ="SF16 and MR816";
o.inDevice = "JackRouter";
//o.outDevice = "Soundflower (16ch)";
// o.inDevice="Yamaha Steinberg FW";
o.outDevice="Yamaha Steinberg FW";
//o.outDevice="Built-in Output";
s.boot;
);

(
MIDIClient.init;
~mOut = MIDIOut(0);
~primes = [31,37,41,43,47,53,57,61,67,71];
TempoClock.default.tempo = 0.8;
~samplerate = 48000;
~numchans = 2;
)


////////////////////////////////////////////////////
// @TODO - Modify routing so that all sound originates at a bus from \dryaudio routing the input to each FX Synth,
////////////////// then on to a global Panning Synth which will have a limiter attached.

(
SynthDef(\dryaudio, { | amp = 1, dryout = 0, fxout = 1, dur = inf, free_trig = 0 |
	var sound, env;
	env = FreeSelf.kr(free_trig);
	sound = SoundIn.ar([0,1]); // in from sampler ---- 10-11 FOR IN SPDIF 1-2 FROM SOUNDFLOWER
	Out.ar(fxout, sound); // to fx
	Out.ar(dryout, sound);
}).add;

SynthDef(\hw_reverb, { | amp = 1, in = 0, out = 0, free_trig = 0, dur = inf, xfade = 0.5 |
	var sound, revsound, env;
	env = FreeSelf.kr(free_trig);
	sound = In.ar(in,2);
	Out.ar(8, sound); // to reverb
	revsound = SoundIn.ar([8,9]); // from reverb
	sound = sound + (revsound * xfade);
	ReplaceOut.ar(out,sound*amp);
}).add;

SynthDef(\panner, { | amp = 1, out = 0, in = 0, dur = 10, panfreq = 1, panmul = 1, panadd = 0 |
	var sound, env;
	env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);
	sound = In.ar(in,1);
	//sound = SinOsc.ar(440) * 0.2;
	sound = PanAz.ar(~numchans, sound, SinOsc.kr(panfreq,mul:panmul,add:panadd));
	ReplaceOut.ar(out,sound*env);
}).add;

SynthDef(\limiter, { | amp = 0.5, dryin = 0, fxin = 1, panin = 2, out = 0, free_trig = 0 |
	var sound, pansound, fxsound, drysound, env;
	env = FreeSelf.kr(free_trig);	// might be worth making a gui & faders for this
	drysound = In.ar(dryin, 2);
	drysound = drysound;
	fxsound = In.ar(fxin, 2);
	fxsound = fxsound * 0.5;
	pansound = In.ar(panin, ~numchans);
	//pansound = Limiter.ar(pansound,0.5,0.1);
	//sound = Limiter.ar(fxsound,0.6,0.1);	// I TOOK THE DRY SOUND OUT OF HERE

	ReplaceOut.ar(out,fxsound*amp*0.3); // 16 - 17; 16
	Out.ar(out+(~numchans*2),pansound*amp*0.3);	// 20 - 21; 16+(~numchans*2)
	ReplaceOut.ar(out+~numchans,drysound*0.4); // 18 - 19; 16+~numchans
}).add;

// simple ringmod
SynthDef(\ringmodulator, { | amp = 0.5, dur = 1, pan = 0, freq = 56, in = 0, out = 0 |
	var sound, env;
	env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);
	// sound = (SoundIn.ar(10)+SoundIn.ar(11))*0.5;	// convert to mono
	sound = In.ar(in,2);
	sound = (sound ring1: FSinOsc.ar(freq));
	//sound = PanAz.ar(5,sound,pan);
	sound = sound*env;
	//4.do({ sound = AllpassC.ar(sound, 0.05, 0.05.rand, 4) });
	Out.ar(out,sound)
}).add;




// shift_verb_section reworked as a synthdef



SynthDef(\shift_verb, { | amp = 1, dur = 1, pan = 0, in = 0, out = 0 |
	var trig, input, r, freq, sound, env, delaylines, reverb, shift_sound;


	env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);
	//input = (SoundIn.ar(10)+SoundIn.ar(11))*0.5;
	input = In.ar(in,2);

	//this is the shift section
	trig = Dust.kr(5);
	r = Lag.kr(TRand.kr(3.5441, 1.75, trig), TRand.kr(1/5,5,trig));	// stable range
	freq = QuadC.ar((96000/8), r.neg, r, 0, 0.1, 11, 12);
	freq = (freq * 0.057762265).exp.min(4.0).max(1.0);
	shift_sound = PitchShift.ar(input, 0.1, freq, 0.0001, 0.0001);




	//reverb section
	delaylines = (shift_sound + input)/4; // charge delay line
	reverb = Mix.ar(Array.fill(6,{ |i|
	9.do{ |j|
			delaylines = DelayC.ar(delaylines, 5, Rand(0.1,5.0/j), 1 / (2**i), delaylines*0.5); // feedback modified....
		};
		delaylines = DelayC.ar(delaylines, 5, ExpRand(0.1,5.0/i), 1 / (2**i), delaylines*0.5);
     }));
	sound = input + Limiter.ar(reverb,0.75,0.01);
	// pan a bit (stereo)
	//sound = PanAz.ar(5,sound,pan)*env;


	Out.ar(out,sound);



}).add;

SynthDef(\delay_verb, { | amp = 1, dur = 1, pan = 0, in = 0, out = 0, fb = 0.5 |
	var trig, input, r, freq, sound, env, delaylines, reverb;


	env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);
	input = In.ar(in,2);




	//reverb section
	delaylines = input; // charge delay line
	reverb = Mix.ar(Array.fill(6,{ |i|
	9.do{ |j|
			delaylines = DelayC.ar(delaylines, 5, Rand(0.1,5.0/j), 1 / (2**i), delaylines*fb); // feedback modified....
		};
		delaylines = DelayC.ar(delaylines, 5, ExpRand(0.1,5.0/i), 1 / (2**i), delaylines*fb);
     }));
	sound = Limiter.ar(reverb*0.5,0.75,0.01);


	Out.ar(out,sound*env);



}).add;

SynthDef(\shift, { | amp = 1, dur = 10, degree = -12, in = 0, out = 0 |
	var sound, env;


	env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);



	// in = (SoundIn.ar(10)+SoundIn.ar(11))*0.5;
	sound = In.ar(in,2);
	sound = PitchShift.ar(sound, 0.1, (degree * 0.057762265).exp, 0, 0.0001);
	//sound = PanAz.ar(5,sound,SinOsc.kr(rrand(0.1,1.0)));


	Out.ar(out,sound*env);



}).add;

SynthDef(\fshift, { | amp = 1, dur = 10, freq = 0, lagdur = 0, in = 0, out = 0 |
	var sound, env;


	env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);



	//in = (SoundIn.ar(10)+SoundIn.ar(11))*0.5;
	sound = In.ar(in,2);
	sound = FreqShift.ar(sound, Lag.kr(freq,lagdur)); /// primes needs to be instantiated first
	//sound = PanAz.ar(5,sound,SinOsc.kr(rrand(0.1,1.0)));


	Out.ar(out,sound*env);



}).add;



SynthDef(\quick_delay_s, { | delaytime = 0.333, amp = 1, dur = 2, pan = 0, degree = 0, in = 0, out = 0,freq=0.1,mul=0 |
	var sound, env, ratio;
	env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);
	ratio = (degree * 0.057762265).exp;



	// sound = (SoundIn.ar(10)+SoundIn.ar(11))*0.5;


	sound = In.ar(in,2);




	sound = DelayC.ar(sound,11,delaytime*LFNoise0.kr(freq,add:1,mul:mul));
	sound = PitchShift.ar(sound, 0.01, ratio, 0, 0.0001);
	//sound = PanAz.ar(5,sound,pan);


	Out.ar(out,sound*env);
}).add;







// delay shifting grain thing



SynthDef(\delay_shift, { | amp = 1, inamp = 1, dtime_array = #[1, 3, 5, 7, 9, 11, 13], distortion = 0, feedback = 0, lag = 10, dur = 1, in = 0, out = 0 |
	var input, times, sound, env, dist_k;


	env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);



	//input = (SoundIn.ar(10)+SoundIn.ar(11))*0.5;


	input = In.ar(in,2);




	sound = Mix.ar(Array.fill(10,{
		var l_sound, trigger, freq, octave, dtime;
		dtime = [1,3,5,7,11,13,17,19].choose;
		octave = [-2,-1,1,2].choose;
		trigger = Impulse.kr(dtime_array.choose);


		freq = Drand(Scale.minor.degrees,inf);	// perhaps choose a different scale
		freq = Demand.kr(trigger, 0, freq);
		freq = freq * octave;
		freq = (freq * 0.057762265).exp;




		l_sound = DelayC.ar(input, 20, dtime);// + InFeedback.ar(20, 1); //feedback in... VERY CAREFUL.
		l_sound = PitchShift.ar(l_sound, 0.5, freq, 0, 0.004);
		// OffsetOut.ar(20,l_sound*feedback); // feedback out
		PanAz.ar(2,l_sound,SinOsc.kr(rrand(0.1,0.2)));
	}));
	//distort
	distortion = Lag.kr(distortion, lag); // set this lag to the same number as the routine!
	dist_k = 2*distortion/(1-distortion);
	sound = (1+distortion) * sound / (1+(dist_k * sound.abs));
	sound = Limiter.ar(sound,1.0,0.01);


	Out.ar(out,sound*env);



}).add;

SynthDef(\delay_shift_2, { | amp = 1, inamp = 1, dtime_array = #[1, 3, 5, 7, 9, 11, 13], distortion = 0, feedback = 0, lag = 10, dur = 1, in = 0, out = 0 |
	var input, times, sound, env, dist_k;


	env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);



	//input = (SoundIn.ar(10)+SoundIn.ar(11))*0.5;


	input = In.ar(in,2);




	sound = Mix.ar(Array.fill(15,{
		var l_sound, trigger, freq, octave, dtime;
		dtime = [1,3,5,7,11,13,17,19].choose;
		octave = [-2,-1,1,2].choose;
		trigger = Impulse.kr(dtime_array.choose);


		freq = Drand(Scale.minor.degrees,inf);	// perhaps choose a different scale
		freq = Demand.kr(trigger, 0, freq);
		freq = freq * octave;
		freq = (freq * 0.057762265).exp;




		l_sound = DelayC.ar(input, 20, dtime);// + InFeedback.ar(20, 1); //feedback in... VERY CAREFUL.
		l_sound = PitchShift.ar(l_sound, 0.5, freq, 0, 0.004);
		// OffsetOut.ar(20,l_sound*feedback); // feedback out
		PanAz.ar(2,l_sound,SinOsc.kr(rrand(0.1,0.2)));
	}));
	//distort
	distortion = Lag.kr(distortion, lag); // set this lag to the same number as the routine!
	dist_k = 2*distortion/(1-distortion);
	sound = (1+distortion) * sound / (1+(dist_k * sound.abs));
	sound = Limiter.ar(sound,1.0,0.01);


	Out.ar(out,sound*env);



}).add;



SynthDef(\quick_delay, { | delaytime = 0.333, amp = 1, dur = 2, pan = 0, degree = 0, in = 0, out = 0 |
	var sound, env, ratio;
	env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);
	ratio = (degree * 0.057762265).exp;



	// sound = (SoundIn.ar(10)+SoundIn.ar(11))*0.5;


	sound = In.ar(in,2);




	sound = DelayC.ar(sound,11,delaytime);
	sound = PitchShift.ar(sound, 0.01, ratio, 0, 0.004);
	//sound = PanAz.ar(5,sound,pan);


	Out.ar(out,sound*env);
}).add;

SynthDef(\tank, { | dur = 10, amp = 1, in = 0, out = 0 |
	var local, env;
	env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);

//	in = (SoundIn.ar(10)+SoundIn.ar(11))*0.5;
	local = In.ar(in,2);

	4.do { local = AllpassC.ar(in, 0.03, {Rand(0.005,0.02)}.dup, 1); };
	local = LocalIn.ar(2) * 0.98;
	local = OnePole.ar(local, 0.5);
	local = AllpassC.ar(local, 0.05, Rand(0.01,0.05), 2);
	local = DelayC.ar(local, 0.3, Rand(0.79,0.26));
	local = AllpassC.ar(local, 0.05, Rand(0.03,0.15), 2);
	local = LeakDC.ar(local);
	local = local + in;
	LocalOut.ar(local*0.5);
	//local = PanAz.ar(5,local,SinOsc.kr(rrand(0.1,0.25)));

	Out.ar(out,local*env);
}).add;

SynthDef(\tank_dist, { |dur = 10, amp = 1, distortion = 0, in = 0, out = 0 |
	var local, env, dist_k;
	env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);

	// in = (SoundIn.ar(10)+SoundIn.ar(11))*0.5;
	local = In.ar(in,2);

	4.do { local = AllpassC.ar(in, 0.03, {Rand(0.005,0.02)}.dup, 1); };
	local = LocalIn.ar(2) * 0.98;
	local = OnePole.ar(local, 0.5);
	local = AllpassC.ar(local, 0.05, Rand(0.01,0.05), 2);
	local = DelayC.ar(local, 0.3, Rand(0.79,0.26));
	local = AllpassC.ar(local, 0.05, Rand(0.03,0.15), 2);
	local = LeakDC.ar(local);
	local = local + in;
	LocalOut.ar(local*0.5);

	dist_k = 2*distortion/(1-distortion);



	local = (1+distortion) * local / (1+(dist_k * local.abs));
	local = Limiter.ar(local,1.0,0.01);



	//local = PanAz.ar(5,local,SinOsc.kr(rrand(0.1,0.25)));

	Out.ar(out,local*env);
}).add;

SynthDef(\rec_buf, { | dur = 10, amp = 1, bufnum = 0, in = 0 |
	var sound, env;
	//sound = SoundIn.ar([10,11]);
	sound = In.ar(in,2);

	env = Line.kr(0,1,dur,doneAction:2);
	RecordBuf.ar(sound, bufnum, doneAction: 2, loop: 0);
}).add;

SynthDef(\play_buf, { | dur = 10, amp = 1, rate = 1, bufnum = 0, pan = 0, out = 0 |
	var sound, env;
	env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);
	sound = PlayBuf.ar(2, bufnum, rate, doneAction: 2);
	//DetectSilence.ar(sound, doneAction: 2); // just in case I accidentally record a tiny bit into the 71 second buffer and overshoot
	//sound = PanAz.ar(5,sound,pan); // stereo file may break this
	Out.ar(out,sound*env);
}).add;

SynthDef(\play_buf_d, { | dur = 10, amp = 1, degree = 0, bufnum = 0, pan = 0, out = 0 |
	var sound, env,rate;
	env = EnvGen.kr(Env.linen(dur/10, (dur*8)/10, dur/10, amp), doneAction: 2);
	rate = (degree * 0.057762265).exp;
	sound = PlayBuf.ar(2, bufnum, rate, doneAction: 2);
	//DetectSilence.ar(sound, doneAction: 2); // just in case I accidentally record a tiny bit into the 71 second buffer and overshoot
	//sound = PanAz.ar(5,sound,pan); // stereo file may break this
	Out.ar(out,sound*env);
}).add;
)

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


(
(
~input.free; ~fx.free; ~reverb.free; ~pan.free; ~output.free;
~master_dry_bus.free; ~master_fx_bus.free; ~master_pan_bus.free;

~input = Group.new(s,\addToHead);	// from sampler
~fx = Group.new(s, \addToTail); // fx chain
~pan = Group.new(s, \addToTail); // from fx, split to surround
~reverb = Group.new(s, \addToTail); // from fx to hardware reverb (stereo)
~output = Group.new(s, \addToTail); // mix, limit, and output (splay reverb?)
~master_dry_bus = Bus.audio(s,2);
~master_fx_bus = Bus.audio(s,2);
~master_pan_bus = Bus.audio(s,~numchans);

s.queryAllNodes;
);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// cleanup in case I quit early
(
~dryaudio.free;
~hwreverb.free;
~limiter.free;
~buf_1.free;
~buf_2.free;
);
// alloc buffers
~buf_1 = Buffer.alloc(s, ~samplerate * 71, 2); // 2 channel 71 second buffer (this will be the longest it can be)
~buf_2 = Buffer.alloc(s, ~samplerate * 71, 2); // 2 channel 71 second buffer (this will be the longest it can be)

// persistent synthdefs
(
// send out 4 channels so I can grab the dry signal with recbuf
// dry audio
~dryaudio = Synth(\dryaudio,
	[\amp, 0.85, \fxout, ~master_fx_bus, \dryout, ~master_dry_bus],
	target: ~input, addAction: \addToHead);
// global hw reverb
//~hwreverb = Synth(\hw_reverb, [\amp, 1, \in, ~master_fx_bus, \out, ~master_fx_bus], target: ~reverb, //addAction:\addToTail);
// output / limiter
~limiter = Synth(\limiter, [\amp,1, \fxin, ~master_fx_bus, \panin, ~master_pan_bus, \dryin,~master_dry_bus, \out,0],target:~output,addAction:\addToTail);
);

s.queryAllNodes;
(
// choose lengths from the prime array for these sections
~section1_dur = (~primes.choose); // for recording must be over a certain amount
~section2_dur = ~primes.choose;
~section3_dur = (~primes.choose).max(~section1_dur); // I don't really want it to transpose up
~section4_dur = ~primes.choose;
~section5_dur = ~primes.choose;
~section6_dur = ~primes.choose;
~section7_dur = ~primes.choose;
~section8_dur = ~primes.choose;
~section9_dur = ~primes.choose;
~section10_dur = ~primes.choose;
~section11_dur = ~primes.choose;
~section12_dur = ~primes.choose;
~section13_dur = ~primes.choose;
~section14_dur = ~primes.choose;
~section15_dur = ~primes.choose;

postln("Section Lengths");
post("1: "); ~section1_dur.postln;
post("2: "); ~section2_dur.postln;
post("3: "); ~section3_dur.postln;
post("4: "); ~section4_dur.postln;
post("5: "); ~section5_dur.postln;
post("6: "); ~section6_dur.postln;
post("7: "); ~section7_dur.postln;
post("8: "); ~section8_dur.postln;
post("9: "); ~section9_dur.postln;
post("10: "); ~section10_dur.postln;
post("11: "); ~section11_dur.postln;
post("12: "); ~section12_dur.postln;
post("13: "); ~section13_dur.postln;
post("14: "); ~section14_dur.postln;
post("15: "); ~section15_dur.postln;

);

postln("Start");
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 1
(
~section1_piano1 = Pfindur(~section1_dur,
	Pbind(
		\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
		\chan, 1,
		\midinote,
			Pxrand([
				(Pseq(Array.series(24,59,rrand(2,12)),3).fold(90,105)),
				(Pseq(
					[
						(Pseq(~primes,3).fold(90,105)),
						(Pseq(~primes,5).fold(92,107)),
						(Pseries(0,Pbrown(1, 12, 1, Prand(~primes.min(13),4)),2).fold(91,106))
					],1))
			], inf),
		\dur,
			Pxrand([
				(Pseq([Pn(1/13,13),Pn(1/7,7),Pn(1/5,5),Pn(1/3,3),1],1)), //5
				(Pseq(
					[
						(Pseq([Pn(1/13,13),Pn(1/7,8),Pn(1/5,5),Pn(1/3,3),1],3)),
						(Pseq([Pn(1/13,13),Pn(1/7,8),Pn(1/5,5),Pn(1/3,3),1],2)),
						(Pseq([Pn(1/13,13),Pn(1/7,8),Pn(1/5,5)],2)),
					],1)),
				2
			], inf),
		\amp, Pexprand(0.5,0.75,inf)
	)//.collect({|event| ~piano1_event = event;})
);

~section1_fx1 = Pfindur(~section1_dur,
		Pbind(
			\instrument, \ringmodulator,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section1_dur/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.25,1.0,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\freq, (Pseq(~primes,3).fold(90,105)) //Pfunc({~piano1_event[\freq].midicps})
		)
);

~section1_fx2 = Pfindur(~section1_dur,
		Pbind(
			\instrument, \rec_buf,
			\group, ~fx,
			\in, ~master_dry_bus,
			\dur, ~section1_dur,
			\amp, 1.0,
			\bufnum, ~buf_1
		)
);

~section1_pan1 = Pfindur(~section1_dur,
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~section1_dur,
			\amp, 1.0,
			\panfreq, 0.025,
			\panmul, 0.025
		)
);
);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 2

(
~section2_piano1 = Pfindur(~section2_dur,
		Pbind(
			\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
			\chan, 1,
			\midinote, Pseries(0,Pbrown(1, 12, 1, Prand([1,3,5,7,11,13],inf)),inf).fold(Pseries(90,-0.1),Pseries(105,-0.1)),
			\dur,
				Pwrand([
					Prand([Pn(1/7,7),Pn(1/5,5),Pn(1/9,9),Pn(1/6,6)], 3),
					Pseq([Pn(1/13,13),Pn(1/7,7),Pn(1/5,5),Pn(1/6,6)],2),
					2,
					3
				], [0.6,0.4,0.4].normalizeSum,inf),
			\amp, Pexprand(0.5,0.75,inf)
		)
);

~section2_fx1 = Pfindur(~section2_dur,
		Pbind(
			\instrument, \shift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section2_dur/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.5,1.0,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\degree, Pwhite(-0.5,0.5,inf)
		), ~section2_dur // tolerance
);
~section2_fx2 = Pfindur(~section2_dur,
		Pbind(
			\instrument, \ringmodulator,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section2_dur/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.5,1.0,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\freq, Pseq(~primes,inf).fold(90-36,105-36).midicps
		), ~section2_dur // tolerance
);
~section2_fx3 = Pfindur(~section2_dur,
		Pbind(
			\instrument, \quick_delay,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section2_dur/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.75,1.0,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\delaytime, Prand([1,3,5,7], inf),
			\degree, -12
		)
);
~section2_fx4 = Pfindur(~section2_dur,
		Pbind(
			\instrument, \quick_delay,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section2_dur/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.75,1.0,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\delaytime, Prand([1,3,5,7], inf),
			\degree, 12
		)
);

~section2_pan1 = Pfindur(~section2_dur,
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~section2_dur,
			\amp, 1.0,
			\panfreq, 0.1,
			\panmul, 0.15
		)
);
);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 3

(
~section3_piano1 = Pfindur(~section3_dur,
		Pbind(
			\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
			\chan, 1,
			\midinote,
				[
					(Prand(Scale.minor.degrees,inf)+70).asStream,
					(Prand(Scale.minor.degrees,inf)+70).asStream,
					(Prand(Scale.minor.degrees,inf)+70).asStream,
					(Prand(Scale.minor.degrees,inf)+70).asStream
				],
			\dur, Pwrand([1,2,3], [0.6,0.4,0.2].normalizeSum,inf),
			\amp, Pexprand(0.5,1.0,inf)
		)
);

~section3_fx1 = Pfindur(~section3_dur,
		Pbind(
			\instrument, \shift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section3_dur/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.5,0.75,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\degree, Pwhite(-0.5,0.5,inf)
		), ~section3_dur // tolerance
);

~section3_fx2 = Pfindur(~section3_dur,
		Pbind(
			\instrument, \ringmodulator,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section3_dur/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.5,0.75,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\freq, Pseq(~primes,inf).fold(90-36,105-36).midicps
		), ~section3_dur // tolerance
);

~section3_fx3 = Pfindur(~section3_dur,
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~section3_dur,
			\amp, Pexprand(0.5,0.9,inf),
			\bufnum, ~buf_1,
			\rate, Pfunc({((~section1_dur/~section3_dur));}),
			\pan, Pwhite(1.0,-1.0,inf)
		)
);

~section3_fx4 = Pfindur(~section3_dur,
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~section3_dur,
			\amp, Pexprand(0.5,0.9,inf),
			\bufnum, ~buf_1,
			\rate, Pfunc({((~section1_dur/~section3_dur)/1.5);}),
			\pan, Pwhite(0.5,-0.5,inf)
		)
);

~section3_pan1 = Pfindur(~section3_dur,
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~section3_dur,
			\amp, 1.0,
			\panfreq, 0.1,
			\panmul, 0.75
		)
);
);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 4

(
~section4_piano1 = Pfindur(~section4_dur,
		Pbind(
			\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
			\chan, 1,
			\midinote,
				[
					(Prand(Scale.minor.degrees,inf)+Pseries(70,-1,inf)).asStream.fold(21,70),
					(Prand(Scale.minor.degrees,inf)+Pseries(70,-0.7,inf)).asStream.fold(21,70),
					(Prand(Scale.minor.degrees,inf)+Pseries(70,-0.5,inf)).asStream.fold(21,70),
					(Prand(Scale.minor.degrees,inf)+Pseries(70,-0.3,inf)).asStream.fold(21,70)
				],
			\dur, Pwrand([1,2,3], [0.6,0.4,0.2].normalizeSum,inf),
			\amp, Pexprand(0.5,1.0,inf)
		)
);

~section4_fx1 = Pfindur(~section4_dur,
		Pbind(
			\instrument, \shift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section4_dur/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.5,1.0,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\degree, Pwhite(-0.5,0.5,inf)
		)
);

~section4_fx2 = Pfindur(~section4_dur,
		Pbind(
			\instrument, \ringmodulator,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section4_dur/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.5,1.0,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\freq, Pseq(~primes,inf).fold(90-36,105-36).midicps
		)
);

~section4_fx3 = Pfindur(~section4_dur,
		Pbind(
			\instrument, \delay_shift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section4_dur+(~section5_dur/2),
			\amp, 0.65,
			\pan, Pwhite(-1.0,1.0,inf)
		)
);

~section4_fx4 = Pfindur(~section4_dur,
		Pbind(
			\instrument, \quick_delay,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section4_dur/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.75,1.0,inf),
			\pan, Pwhite(-1.0,1.0,inf),
			\delaytime, Prand([1,3,5,7],inf)
		)
);

~section4_fx5 = Pfindur(~section4_dur,
		Pbind(
			\instrument, \rec_buf,
			\group, ~fx,
			\addAction, \addToHead,
			\in, ~master_dry_bus,
			\dur, ~section4_dur,
			\amp, 1.0,
			\bufnum, ~buf_1
		)
);

~section4_pan1 = Pfindur(~section4_dur,
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~section4_dur+(~section4_dur*8/10),
			\amp, 1.0,
			\panfreq, 0.025,
			\panmul, 0.5
		)
);
);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 5

(
~section5_piano1 = Pfindur(~section5_dur,
		Pbind(
			\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
			\chan, 1,
			\midinote, Pstutter(x=Prand(~primes,inf),Pwhite(67,77,inf)),
			\dur, Pwrand([Pn(1/5,5),Pn(1/11,11),Pn(1/7,7),1], [0.4,0.3,0.4,0.6].normalizeSum,inf),
			\amp, Pseq([(Pseq(((1..100)/100),1)*Pwhite(1.1,0.9,inf)),(Pseq(((100..1)/100),1)*Pwhite(1.1,0.9,inf))],inf)
		)
);

~section5_fx1 = Pfindur(~section5_dur,
		Pbind(
			\instrument, \shift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section5_dur/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.5,0.75,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\degree, Pwhite(-0.5,0.5,inf)
		)
);

~section5_fx2 = Pfindur(~section5_dur,
		Pbind(
			\instrument, \ringmodulator,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section5_dur/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.25,0.5,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\freq, Pseq(~primes,inf).fold(90-36,105-36).midicps
		)
);

~section5_fx3 = Pfindur(~section5_dur,
		Pbind(
			\instrument, \delay_shift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section5_dur+(~section5_dur*8/10),
			\amp, Pexprand(0.5,0.7,inf),
			\pan, Pwhite(-1.0,1.0,inf)
		)
);

~section5_fx4 = Pfindur(~section5_dur,
		Pbind(
			\instrument, \quick_delay,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section5_dur/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.75,1.0,inf),
			\pan, Pwhite(-1.0,1.0,inf),
			\delaytime, Prand([1,3,5,7,11],inf),
			\degree, Prand([-11,-7,-5,-3,-2,-1,0,1,2,3,5,7,11],inf)
		)
);

~section5_fx5 = Pfindur(~section5_dur,
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~section5_dur,
			\amp, Pexprand(0.75,1.0,inf),
			\bufnum, ~buf_1,
			\rate, Pfunc({((~section4_dur/~section5_dur));}),
			\pan, Pwhite(0.5,-0.5,inf)
		)
);

~section5_fx6 = Pfindur(~section5_dur,
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~section5_dur,
			\amp, Pexprand(0.75,1.0,inf),
			\bufnum, ~buf_1,
			\rate, Pfunc({((~section4_dur/~section5_dur))*1.1;}),
			\pan, Pwhite(0.5,-0.5,inf)
		)
);
~section5_fx7 = Pfindur(~section5_dur,
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~section5_dur,
			\amp, Pexprand(0.75,1.0,inf),
			\bufnum, ~buf_1,
			\rate, Pfunc({((~section4_dur/~section5_dur))*1.3;}),
			\pan, Pwhite(0.5,-0.5,inf)
		)
);

~section5_fx8 = Pfindur(~section5_dur,
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~section5_dur,
			\amp, Pexprand(0.75,1.0,inf),
			\bufnum, ~buf_1,
			\rate, Pfunc({((~section4_dur/~section5_dur))*1.5;}),
			\pan, Pwhite(0.5,-0.5,inf)
		)
);

~section5_fx9 = Pfindur(~section5_dur,
		Pbind(
			\instrument, \rec_buf,
			\addAction, \addToHead,
			\group, ~fx,
			\in, ~master_dry_bus, // record fx
			\dur, ~section5_dur,
			\amp, 1.0,
			\bufnum, ~buf_2 // use another buffer!
		)
);

~section5_pan1 = Pfindur(~section5_dur,
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~section5_dur,
			\amp, 0.5,
			\panfreq, 0.02,
			\panmul, 0.3
		)
);
);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 6

(
~section6_piano1 = Pfindur(~section6_dur,
		Pbind(
			\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
			\chan, 3,
			\midinote, [
				50,
				Pwhite(40,45).asStream,
				Pwhite(45,50).asStream,
				Pwhite(50,55).asStream,
				Pwhite(55,60).asStream
				],
			\dur, Prand([2,1,3,5,7], inf),
			\amp, Pexprand(0.5,1.0,inf),
			\legato, Pkey(\dur)*Pwhite(1.0,1.25,inf)

		)
);


~section6_fx1 = Pfindur(~section6_dur,
		Pbind(
			\instrument, \fshift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section6_dur/Prand([5,7,11,13],inf),
			\lagdur, Pkey(\dur),
			\amp, 0.5,
			\pan, Pwhite(-1.0,1.0,inf),
			\freq, (Prand(~primes,inf).midicps)*Prand([-5,-3,-1,1,3,5,7],inf)
		)
);

~section6_fx2 = Pfindur(~section6_dur,
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~section5_dur,
			\amp, Pexprand(0.75,1.0,inf),
			\bufnum, ~buf_2,
			\rate, Pfunc({((~section5_dur/~section6_dur))/1;}),
			\pan, Pwhite(0.5,-0.5,inf)
		)
);

~section6_fx3 = Pfindur(~section6_dur,
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~section5_dur,
			\amp, Pexprand(0.75,1.0,inf),
			\bufnum, ~buf_2,
			\rate, Pfunc({((~section5_dur/~section6_dur))/1.3;}),
			\pan, Pwhite(0.5,-0.5,inf)
		)
);

~section6_fx4 = Pfindur(~section6_dur,
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~section5_dur,
			\amp, Pexprand(0.75,1.0,inf),
			\bufnum, ~buf_2,
			\rate, Pfunc({((~section5_dur/~section6_dur))/1.5;}),
			\pan, Pwhite(0.5,-0.5,inf)
		)
);

~section6_pan1 = Pfindur(~section6_dur,
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~section6_dur,
			\amp, 1.0,
			\panfreq, 0.1,
			\panmul, 0.1
		)
);
);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 7

(
~section7_piano1 = Pfindur(~section7_dur,
		Pbind(
			\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
			\chan, 1,
			\midinote, [
				50,
				Pwhite(30,40).asStream,
				Pwhite(40,45).asStream,
				Pwhite(55,65).asStream,
				Pwhite(65,77).asStream
				],
			\dur, Prand([2,1,3,5], inf),
			\amp, Pexprand(0.75,1.0,inf)
		)
);


~section7_fx1 = Pfindur(~section7_dur,
		Pbind(
			\instrument, \fshift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section7_dur/Prand([1,3,5,7],inf),
			\lagdur, Pkey(\dur),
			\amp, Pexprand(0.75,1.0,inf),
			\pan, Pwhite(-1.0,1.0,inf),
			\freq, (Prand(~primes,inf).midicps)*Prand([1,3,5,7,11],inf)
		)
);

~section7_fx2 = Pfindur(~section7_dur,
		Pbind(
			\instrument, \delay_shift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section7_dur/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.5,1.0,inf),
			\pan, Pwhite(-1.0,1.0,inf)
		)
);

~section7_fx3 = Pfindur(~section7_dur,
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~section7_dur,
			\amp, Pexprand(0.5,1.0,inf),
			\bufnum, ~buf_2,
			\rate, Pfunc({((~section5_dur/~section7_dur));}),
			\pan, Pwhite(1.0,-1.0,inf)
		)
);

~section7_fx4 = Pfindur(~section7_dur,
		Pbind(
			\instrument, \play_buf,
			\dur, ~section7_dur,
			\group, ~fx,
			\out, ~master_fx_bus,
			\amp, Pexprand(0.5,1.0,inf),
			\bufnum, ~buf_1,
			\rate, Pfunc({((~section1_dur/~section7_dur)*1.1);}),
			\pan, Pwhite(0.5,-0.5,inf)
		)
);

~section7_fx5 = Pfindur(~section7_dur,
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~section7_dur,
			\amp, Pexprand(0.5,1.0,inf),
			\bufnum, ~buf_1,
			\rate, Pfunc({((~section1_dur/~section7_dur)*1.3);}),
			\pan, Pwhite(0.5,-0.5,inf)
		)
);

~section7_fx6 = Pfindur(~section7_dur,
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~section7_dur,
			\amp, Pexprand(0.5,1.0,inf),
			\bufnum, ~buf_1,
			\rate, Pfunc({((~section1_dur/~section7_dur)*1.5);}),
			\pan, Pwhite(0.5,-0.5,inf)
		)
);

~section7_fx7 = Pfindur(~section7_dur,
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~section7_dur,
			\amp, Pexprand(0.5,1.0,inf),
			\bufnum, ~buf_2,
			\rate, Pfunc({((~section5_dur/~section7_dur)*1.7);}),
			\pan, Pwhite(0.5,-0.5,inf)
		)
);

~section7_pan1 = Pfindur(~section7_dur,
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~section7_dur,
			\amp, 1.0,
			\panfreq, 0.25,
			\panmul, 0.75
		)
);
);

(
~section8_piano1 = Pfindur(~section8_dur,
		Pbind(
			\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
			\chan, Pstutter(2,Pseq([1,0,3,1,0,3,4],inf)),
			\midinote, [
				50,
				Pwhite(30,40).asStream,
				Pwhite(40,45).asStream,
				Pwhite(55,65).asStream,
				Pwhite(65,77).asStream
				],
			\dur, Prand([2,1,3,5], inf),
			\amp, Pexprand(0.75,1.0,inf)
		)
);

~section8_fx1 = Pfindur(~section8_dur,
		Pbind(
			\instrument, \delay_verb,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\fb, 0.6,
			\dur, ~section8_dur+(~section8_dur*8/10),
			\amp, 0.75
		)
);

~section8_fx2 = Pfindur(~section8_dur,
		Pbind(
			\instrument, \ringmodulator,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section8_dur/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.5,1.0,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\freq, 30.midicps
		), ~section8_dur // tolerance
);

~section8_pan1 = Pfindur(~section8_dur,
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~section8_dur,
			\amp, 1.0,
			\panfreq, 0.1,
			\panmul, 0.25
		)
);
);// end of section 1

(
~section9_piano1 = Pfindur(~section9_dur,
	Pbind(
		\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
		\chan, 0,
		\midinote, 	Prand([79],inf)+Pstutter(Prand([3,5,7],inf),Prand(Scale.phrygian.degrees,inf)),
		\dur, Prand([Pn(1/3,3),Pn(1/5,5),1,Pn(1/2,2)],inf)*2,
		\amp, Pexprand(0.6,0.75,inf)
	)
);

~section9_piano2 = Pfindur(~section9_dur,
	Pbind(
		\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
		\chan, 0,
		\midinote, 	Prand([79],inf)+Pstutter(Prand([3,5,7],inf),Prand(Scale.phrygian.degrees,inf)),
		\dur, Prand([Pn(1/4,4),Pn(1/6,6),1,Pn(1/2,2)],inf)*2,
		\amp, Pexprand(0.6,0.75,inf)
	)
);

~section9_fx1 = Pfindur(~section9_dur,
		Pbind(
			\instrument, \delay_verb,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\fb, 0.5,
			\dur, ~section9_dur+(~section9_dur*8/10),
			\amp, 0.8
		)
);

~section9_fx3 = Pfindur(~section9_dur,
		Pbind(
			\instrument, \ringmodulator,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section9_dur/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.5,0.75,inf),
			\freq, Prand([79,79-12,79-24,79-48],inf).midicps
		)
);

~section9_pan1 = Pfindur(~section9_dur,
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~section9_dur,
			\amp, 1.0,
			\panfreq, 0,
			\panmul, 0.25
		)
);
);

(
~section10_piano1 = Pfindur(~section10_dur,
	Pbind(
		\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
		\chan, 0,
		\midinote,
			[
				Prand(Scale.minor.degrees+(79-24),inf).asStream,
				Prand(Scale.minor.degrees+(79-24),inf).asStream,
				Prand(Scale.minor.degrees+(79-12),inf).asStream,
				Prand(Scale.minor.degrees+(79-12),inf).asStream
			],
		\dur, Pxrand([Pseq([1/3,2/3],1),1,4,3,2],inf),
		\legato, 6,
		\amp, Pexprand(0.6,0.8,inf)
	)
);


~section10_piano2 = Pfindur(~section10_dur,
	Pbind(
		\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
		\chan, 1,
		\midinote,
			Pxrand([
				(Pseq(Array.series(24,59,rrand(2,12)),3).fold(90,105)),
				(Pseq(
					[
						(Pseq(~primes,3).fold(90,105)),
						(Pseq(~primes,5).fold(92,107)),
						(Pseries(0,Pbrown(1, 12, 1, Prand(~primes.min(13),4)),2).fold(91,106))
					],1))
			], inf),
		\dur,
			Pxrand([
				(Pseq([Pn(1/13,13),Pn(1/7,7),Pn(1/5,5),Pn(1/3,3),1],1)), //5
				(Pseq(
					[
						(Pseq([Pn(1/13,13),Pn(1/7,8),Pn(1/5,5),Pn(1/3,3),1],3)),
						(Pseq([Pn(1/13,13),Pn(1/7,8),Pn(1/5,5),Pn(1/3,3),1],2)),
						(Pseq([Pn(1/13,13),Pn(1/7,8),Pn(1/5,5)],2)),
					],1)),
				2
			], inf)/4,
		\amp, Pstutter(29,Pwrand([0,Pexprand(0.3,0.6,1)],[0.999,0.001],inf))
	)//.collect({|event| ~piano1_event = event;})
);

~section10_fx1 = Pfindur(~section10_dur,
		Pbind(
			\instrument, \quick_delay,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section10_dur+(~section10_dur*8/10),
			\amp, 1.0,
			\delaytime, Prand([1,2,3,5,7],inf),
			\degree, Prand([-1,-2,-3,-5,-7,1,2,3,5,7],inf)
		)
);

~section10_pan1 = Pfindur(~section10_dur,
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~section10_dur,
			\amp, 1.0,
			\panfreq, 0.2,
			\panmul, 0.17
		)
);
);

(
~section11_piano1 = Pfindur(~section11_dur,
	Pbind(
		\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
		\chan, 0,
		\midinote,
			[
				Prand(Scale.minor.degrees+(79-36),inf).asStream,
				Prand(Scale.minor.degrees+(79-24),inf).asStream,
				Prand(Scale.minor.degrees+(79-12),inf).asStream,
				Prand(Scale.minor.degrees+(79),inf).asStream
			],
		\dur, Pxrand([Pseq([1/3,2/3],1),1,4,3,2],inf),
		\legato, 6,
		\amp, Pexprand(0.6,0.9,inf)
	)
);

~section11_fx1 = Pfindur(~section11_dur,
		Pbind(
			\instrument, \quick_delay,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section11_dur+(~section11_dur*8/10),
			\amp, 1.0,
			\delaytime, Prand([1,2,3,5,7],inf),
			\degree, Prand([-1,-2,-3,-5,-7,1,2,3,5,7],inf)
		)
);

~section11_fx2 = Pfindur(~section11_dur,
		Pbind(
			\instrument, \fshift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, (~section11_dur/(Prand([5,7,11,13,17],inf)))+(~section11_dur*8/10),
			\amp, 1.0,
			\lagdur, Prand([1,2,3,5,7],inf),
			\freq, 79.midicps/Prand([-1,-2,-3,-5,-7,1,2,3,5,7],inf)
		)
);


~section11_fx3 = Pfindur(~section11_dur,
		Pbind(
			\instrument, \rec_buf,
			\group, ~fx,
			\in, ~master_dry_bus,
			\dur, ~section11_dur,
			\amp, 1.0,
			\bufnum, ~buf_1
		)
);

~section11_pan1 = Pfindur(~section11_dur,
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~section11_dur,
			\amp, 1.0,
			\panfreq, 0.1,
			\panmul, 0.4
		)
);
);

(
~section12_piano1 = Pfindur(~section12_dur,
	Pbind(
		\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
		\chan, 0,
		\midinote,
			[
				Prand(Scale.diminished.degrees+(79-36-2),inf).asStream,
				Prand(Scale.diminished.degrees+(79-24-2),inf).asStream,
				Prand(Scale.diminished.degrees+(79-12-2),inf).asStream,
				Prand(Scale.diminished.degrees+(79-2),inf).asStream
			],
		\dur, Pxrand([Pseq([1/3,2/3],1),1/2,1,8,2,4],inf),
		\legato, 6,
		\amp, Pexprand(0.6,0.9,inf)
	)
);

~section12_piano2 = Pfindur(~section12_dur,
	Pbind(
		\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
		\chan, 0,
		\midinote, Prand([58,62,63,64,67,68,69,70,75],inf),
		\dur, Pxrand([1,2,3,1/2,1/4,1/8],inf),
		\amp, Pexprand(0.25,8,inf)
	)
);

~section12_fx1 = Pfindur(~section12_dur,
		Pbind(
			\instrument, \quick_delay_s,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section5_dur+(~section5_dur*8/10),
			\amp, Pexprand(0.4,0.75),
			\delaytime, Prand([1,2,3,5,7],inf),
			\degree, Prand([-1,-2,-3,-5,-7,1,2,3,5,7],inf),
			\freq, Pkey(\delaytime)/10,
			\mul, Pseries(0,Pexprand(0.1,0.5),inf)
		)
);

~section12_pan1 = Pfindur(~section12_dur,
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~section12_dur,
			\amp, 1.0,
			\panfreq, 0.2,
			\panmul, 0.2
		)
);
);

(
~section13_piano1 = Pfindur(~section13_dur,
	Pbind(
		\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
		\chan, 0,
		\midinote, Prand([58,62,63,64,67,68,69,70,75],inf),
		\dur, Pseq([1,2,3],inf)/Pseq([Pn(2,4),Pn(3,3),5,5,7,7,11,11,13,17],inf),
		\amp, Pexprand(0.25,8,inf)
	)
);

~section13_fx1 = Pfindur(~section13_dur,
		Pbind(
			\instrument, \quick_delay,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section13_dur/Prand([1,3,5,7]),
			\amp, 1.0,
			\delaytime, Prand([1,2,3,5,7],inf),
			\degree, Prand([-1,-2,-3,-5,-7,1,2,3,5,7],inf)
		)
);

~section13_fx2 = Pfindur(~section13_dur,
		Pbind(
			\instrument, \play_buf_d,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~section13_dur,
			\amp, Pexprand(0.7,1.0,inf),
			\degree, Prand([3,5,7,-2,-4,-6,-8,-10,-12],inf)
		)
);

~section13_pan1 = Pfindur(~section13_dur,
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~section13_dur,
			\amp, 0.5,
			\panfreq, 0.2,
			\panmul, 0.7
		)
);
);

(
~section14_piano1 = Pfindur(~section14_dur,
	Pbind(
		\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
		\chan, 0,
		\midinote, Prand([58,62,63,64,67,68,69,70,75],inf)+Pstutter(29,Pseries(0,1,30)),
		\dur,
			Prand([
				(Pseq([Pn(1/13,13),Pn(1/7,7),Pn(1/5,5),Pn(1/3,3),1],1)), //5
				(Pseq(
					[
						(Pseq([Pn(1/12,12),Pn(1/6,6),Pn(1/4,4),Pn(1/2,2),1],3)),
						(Pseq([Pn(1/13,13),Pn(1/7,7),Pn(1/5,5),Pn(1/3,3),1],2)),
					],1)),
				2
			], inf)*4,
		\amp, Pexprand(0.9,1,inf)
	)
);

~section14_fx1 = Pfindur(~section14_dur,
		Pbind(
			\instrument, \rec_buf,
			\group, ~fx,
			\in, ~master_dry_bus,
			\dur, ~section14_dur,
			\amp, 1.0
		)
);
~section14_fx2 = Pfindur(~section14_dur,
		Pbind(
			\instrument, \delay_shift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~section14_dur+(~section14_dur*8/10),
			\amp, Pexprand(0.5,1.0)
			//\dtime_array, #[1, 3, 5, 7, 9, 11, 13]*2
		)
);
~section14_pan1 = Pfindur(~section14_dur,
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~section14_dur,
			\amp, 1.0,
			\panfreq, 0,
			\panmul, 0.25
		)
);
);

(
~section15_piano1 = Pfindur(~section15_dur+(~section15_dur/2),
	Pbind(
		\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
		\chan, 1,
		\midinote,
			Pxrand([
				(Pseq(Array.series(24,59,rrand(2,12)),3).fold(90,105)),
				(Pseq(
					[
						(Pseq(~primes,3).fold(90,105)),
						(Pseq(~primes,5).fold(92,107)),
						(Pseries(0,Pbrown(1, 12, 1, Prand(~primes.min(13),4)),2).fold(91,106))
					],1))
			], inf),
		\dur,
			Pxrand([
				(Pseq([Pn(1/13,13),Pn(1/7,7),Pn(1/5,5),Pn(1/3,3),1],1)), //5
				(Pseq(
					[
						(Pseq([Pn(1/13,13),Pn(1/7,8),Pn(1/5,5),Pn(1/3,3),1],3)),
						(Pseq([Pn(1/13,13),Pn(1/7,8),Pn(1/5,5),Pn(1/3,3),1],2)),
						(Pseq([Pn(1/13,13),Pn(1/7,8),Pn(1/5,5)],2)),
					],1)),
				2
			], inf)*4,
		\amp, Pexprand(0.5,0.75,inf)
	)//.collect({|event| ~piano1_event = event;})
);

~section15_fx1 = Pfindur(~section15_dur,
		Pbind(
			\instrument, \play_buf_d,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~section15_dur,
			\amp, 1.0,
			\degree, Prand([-11,-7,-5,-2,-1,1,2,3,5,7,11],inf)
		)
);

~section15_pan1 = Pfindur(~section15_dur,
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~section15_dur,
			\amp, 1.0,
			\panfreq, 0,
			\panmul, 0.25
		)
);
);


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

~master = Pseq([
	Pfuncn({"Section 1:".postln; 1},1),
	Ppar([~section1_piano1,~section1_fx1, ~section1_fx2, ~section1_pan1]), // (A)
	Pfuncn({"Section 2:".postln; 1},1),
	Ppar([~section2_piano1,
		~section2_fx1,~section2_fx2,~section2_fx3,~section2_fx4,
		~section2_pan1]),
	Pfuncn({"Section 3:".postln; 1},1),
	Ppar([~section3_piano1,
		~section3_fx1,~section3_fx2,~section3_fx3,~section3_fx4,
		~section3_pan1]),
	Pfuncn({"Section 4:".postln; 1},1),
	Ppar([~section4_piano1,
		~section4_fx1,~section4_fx2,~section4_fx3,~section4_fx4, ~section4_fx5,
		~section4_pan1]),
		Pfuncn({"Section 5:".postln; 1},1),
	Ppar([~section5_piano1,
		~section5_fx1,~section5_fx2,~section5_fx3,
		~section5_fx4, ~section5_fx4, ~section5_fx4, ~section5_fx4, ~section5_fx4,
		~section5_fx5, ~section5_fx6,~section5_fx7,~section5_fx8,~section5_fx9,
		~section5_pan1]),
		Pfuncn({"Section 6:".postln; 1},1),
	Ppar([~section6_piano1,
		~section6_fx1, ~section6_fx1,~section6_fx1,
		~section6_fx2, ~section6_fx3,~section6_fx4,
		~section6_pan1]),
		Pfuncn({"Section 7:".postln; 1},1),
	Ppar([~section7_piano1,
		~section7_fx1,~section7_fx2,~section7_fx3,~section7_fx4,
		~section7_fx5,~section7_fx6,~section7_fx7, ~section7_pan1]),
		Pfuncn({"Section 8:".postln; 1},1),
	Ppar([~section8_piano1,~section8_fx1,~section8_pan1]),
		Pfuncn({"Section 9:".postln; 1},1),
	Ppar([~section9_piano1,~section9_piano2,~section9_fx1, ~section9_pan1]),
		Pfuncn({"Section 10:".postln; 1},1),
	Ppar([~section10_piano1,~section10_piano2,
		~section10_fx1, ~section10_fx1,~section10_fx1,~section10_fx1,
		~section10_pan1]),
		Pfuncn({"Section 11:".postln; 1},1),
	Ppar([~section11_piano1,
		~section11_fx2,~section11_fx3,~section11_fx2,
		~section11_fx1, ~section11_fx1,~section11_fx1,~section11_fx1,
		~section11_pan1]),
		Pfuncn({"Section 12:".postln; 1},1),
	Ppar([~section12_piano1,~section12_piano2,
		~section12_fx1,~section12_fx1,~section12_fx1,~section12_fx1,
		~section12_fx1,~section12_fx1,~section12_fx1,~section12_fx1,
		~section12_pan1]),
		Pfuncn({"Section 13:".postln; 1},1),
	Ppar([~section13_piano1,~section13_piano1,
		~section13_fx1,~section13_fx1,~section13_fx1,~section13_fx1,
		~section13_fx2,~section13_fx2,~section13_fx2,~section13_fx2,
		~section13_fx2,~section13_fx2,~section13_fx2,~section13_fx2,
		~section13_pan1]),
		Pfuncn({"Section 14:".postln; 1},1),
	Ptpar([10,~section14_piano1,10,~section14_piano1,10,~section14_piano1,
		10,~section14_fx1, 10,~section14_fx2,
		10,~section14_pan1]),
		Pfuncn({"Section 15:".postln; 1},1),
	Ppar([~section15_piano1,
		~section15_fx1,~section15_fx1,~section15_fx1,~section15_fx1,
		~section15_fx1,~section15_fx1,
		~section15_pan1]),

], 1).play;
)

~master.stop;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

(
~dryaudio.free;
~hwreverb.free;
~limiter.free;
)

/// TESTING SECTION.

s.queryAllNodes;

p=Ppar([~section1_piano1,~section1_fx1, ~section1_fx2, ~section1_pan1]).play;
p=Ppar([~section2_piano1,~section2_fx1,~section2_fx2,~section2_fx3,~section2_fx4]).play;
p=Ppar([~section3_piano1,~section3_fx1,~section3_fx2,~section3_fx3,~section3_fx4]).play;
p=Ppar([~section4_piano1,~section4_fx1,~section4_fx2,~section4_fx3,~section4_fx4, ~section4_fx5, ~section4_pan1]).play;
p=Ppar([~section5_piano1,~section5_fx1,~section5_fx2,~section5_fx4, ~section5_fx4, ~section5_fx4, ~section5_fx4, ~section5_fx4, ~section5_fx5, ~section5_fx6,~section5_fx7,~section5_fx8,~section5_fx9, ~section5_pan1]).play;
p=Ppar([~section6_piano1,~section6_fx1]).play;
p=Ppar([~section7_piano1,~section7_fx1,~section7_fx2,~section7_fx3,~section7_fx4,~section7_fx5,~section7_fx6,~section7_fx7]).play;
p.stop;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
p=Ppar([~section5_piano1,~section5_fx6,~section5_fx7,~section5_fx8,~section5_fx9,~section5_pan1]).play;
