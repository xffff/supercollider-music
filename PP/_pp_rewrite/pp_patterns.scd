/************************************/
/* pp		             	        */
/*	PATTERNS	        			*/
/************************************/

(
~sections = 0!15;
~durations = 0!15;

~primes = [31,37,41,43,47,53,57,61,67,71];

for(0,14, { |i|
	~durations[i] = ~primes.choose;
});
~durations[3] = ~primes.choose.max(~durations[0]);

~sections[0] = Pfindur(~durations[0],
	Ppar([
		// piano 1
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
		),
		//ringmod
		Pbind(
			\instrument, \ringmodulator,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[0]/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.25,1.0,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\freq, (Pseq(~primes,3).fold(90,105))
		),
		//record
		Pbind(
			\instrument, \rec_buf,
			\group, ~fx,
			\in, ~master_dry_bus,
			\dur, ~durations[0],
			\amp, 1.0,
			\bufnum, ~buf_1
		),
		//panner
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~durations[0],
			\amp, 1.0,
			\panfreq, 0.025,
			\panmul, 0.025
		)
	])
);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 2

~sections[1] = Pfindur(~durations[1],
	Ppar([
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
		),
		Pbind(
			\instrument, \shift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[1]/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.5,1.0,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\degree, Pwhite(-0.5,0.5,inf)
		),
		Pbind(
			\instrument, \ringmodulator,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[1]/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.5,1.0,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\freq, Pseq(~primes,inf).fold(90-36,105-36).midicps
		),
		Pbind(
			\instrument, \quick_delay,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[1]/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.75,1.0,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\delaytime, Prand([1,3,5,7], inf),
			\degree, -12
		),
		Pbind(
			\instrument, \quick_delay,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[1]/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.75,1.0,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\delaytime, Prand([1,3,5,7], inf),
			\degree, 12
		),
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~durations[1],
			\amp, 1.0,
			\panfreq, 0.1,
			\panmul, 0.15
		)
	])
);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 3

~sections[2] = Pfindur(~durations[2],
	Ppar([
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
		),
		Pbind(
			\instrument, \shift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[2]/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.5,0.75,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\degree, Pwhite(-0.5,0.5,inf)
		),
		Pbind(
			\instrument, \ringmodulator,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[2]/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.5,0.75,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\freq, Pseq(~primes,inf).fold(90-36,105-36).midicps
		),
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~durations[2],
			\amp, Pexprand(0.5,0.9,inf),
			\bufnum, ~buf_1,
			\rate, Pfunc({((~durations[0]/~durations[2]));}),
			\pan, Pwhite(1.0,-1.0,inf)
		),
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~durations[2],
			\amp, Pexprand(0.5,0.9,inf),
			\bufnum, ~buf_1,
			\rate, Pfunc({((~durations[0]/~durations[2])/1.5);}),
			\pan, Pwhite(0.5,-0.5,inf)
		),
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~durations[2],
			\amp, 1.0,
			\panfreq, 0.1,
			\panmul, 0.75
		)
	])
);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 4

~sections[3] = Pfindur(~durations[3],
	Ppar([
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
		),
		Pbind(
			\instrument, \shift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[3]/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.5,1.0,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\degree, Pwhite(-0.5,0.5,inf)
		),
		Pbind(
			\instrument, \ringmodulator,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[3]/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.5,1.0,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\freq, Pseq(~primes,inf).fold(90-36,105-36).midicps
		),
		Pbind(
			\instrument, \delay_shift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[3]+(~durations[4]/2),
			\amp, 0.65,
			\pan, Pwhite(-1.0,1.0,inf)
		),
		Pbind(
			\instrument, \quick_delay,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[3]/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.75,1.0,inf),
			\pan, Pwhite(-1.0,1.0,inf),
			\delaytime, Prand([1,3,5,7],inf)
		),
		Pbind(
			\instrument, \rec_buf,
			\group, ~fx,
			\addAction, \addToHead,
			\in, ~master_dry_bus,
			\dur, ~durations[3],
			\amp, 1.0,
			\bufnum, ~buf_1
		),
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~durations[3]+(~durations[3]*8/10),
			\amp, 1.0,
			\panfreq, 0.025,
			\panmul, 0.5
		)
	])
);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 5

~sections[4] = Pfindur(~durations[4],
	Ppar([
		Pbind(
			\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
			\chan, 1,
			\midinote, Pstutter(x=Prand(~primes,inf),Pwhite(67,77,inf)),
			\dur, Pwrand([Pn(1/5,5),Pn(1/11,11),Pn(1/7,7),1], [0.4,0.3,0.4,0.6].normalizeSum,inf),
			\amp, Pseq([(Pseq(((1..100)/100),1)*Pwhite(1.1,0.9,inf)),(Pseq(((100..1)/100),1)*Pwhite(1.1,0.9,inf))],inf)
		),
		Pbind(
			\instrument, \shift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[4]/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.5,0.75,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\degree, Pwhite(-0.5,0.5,inf)
		),
		Pbind(
			\instrument, \ringmodulator,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[4]/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.25,0.5,inf),
			\pan, Pwhite(-0.25,0.25,inf),
			\freq, Pseq(~primes,inf).fold(90-36,105-36).midicps
		),
		Pbind(
			\instrument, \delay_shift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[4]+(~durations[4]*8/10),
			\amp, Pexprand(0.5,0.7,inf),
			\pan, Pwhite(-1.0,1.0,inf)
		),
		Pbind(
			\instrument, \quick_delay,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[4]/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.75,1.0,inf).dup(5), // five of these
			\pan, Pwhite(-1.0,1.0,inf),
			\delaytime, Prand([1,3,5,7,11],inf),
			\degree, Prand([-11,-7,-5,-3,-2,-1,0,1,2,3,5,7,11],inf)
		),
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~durations[4],
			\amp, Pexprand(0.75,1.0,inf),
			\bufnum, ~buf_1,
			\rate, Pfunc({((~durations[3]/~durations[4]));}),
			\pan, Pwhite(0.5,-0.5,inf)
		),
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~durations[4],
			\amp, Pexprand(0.75,1.0,inf),
			\bufnum, ~buf_1,
			\rate, Pfunc({((~durations[3]/~durations[4]))*1.1;}),
			\pan, Pwhite(0.5,-0.5,inf)
		),
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~durations[4],
			\amp, Pexprand(0.75,1.0,inf),
			\bufnum, ~buf_1,
			\rate, Pfunc({((~durations[3]/~durations[4]))*1.3;}),
			\pan, Pwhite(0.5,-0.5,inf)
		),
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~durations[4],
			\amp, Pexprand(0.75,1.0,inf),
			\bufnum, ~buf_1,
			\rate, Pfunc({((~durations[3]/~durations[4]))*1.5;}),
			\pan, Pwhite(0.5,-0.5,inf)
		),
		Pbind(
			\instrument, \rec_buf,
			\addAction, \addToHead,
			\group, ~fx,
			\in, ~master_dry_bus, // record fx
			\dur, ~durations[4],
			\amp, 1.0,
			\bufnum, ~buf_2 // use another buffer!
		),
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~durations[4],
			\amp, 0.5,
			\panfreq, 0.02,
			\panmul, 0.3
		)
	])
);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 6

~sections[5] = Pfindur(~durations[5],
	Ppar([
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

		),
		Pbind(
			\instrument, \fshift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[5]/Prand([5,7,11,13],inf),
			\lagdur, Pkey(\dur),
			\amp, 0.5,
			\pan, Pwhite(-1.0,1.0,inf),
			\freq, (Prand(~primes,inf).midicps)*
			Prand([-5,-3,-1,1,3,5,7],inf).dup(3) // 3 of these
		),
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~durations[4],
			\amp, Pexprand(0.75,1.0,inf),
			\bufnum, ~buf_2,
			\rate, Pfunc({((~durations[4]/~durations[5]))/1;}),
			\pan, Pwhite(0.5,-0.5,inf)
		),
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~durations[4],
			\amp, Pexprand(0.75,1.0,inf),
			\bufnum, ~buf_2,
			\rate, Pfunc({((~durations[4]/~durations[5]))/1.3;}),
			\pan, Pwhite(0.5,-0.5,inf)
		),
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~durations[4],
			\amp, Pexprand(0.75,1.0,inf),
			\bufnum, ~buf_2,
			\rate, Pfunc({((~durations[4]/~durations[5]))/1.5;}),
			\pan, Pwhite(0.5,-0.5,inf)
		),
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~durations[5],
			\amp, 1.0,
			\panfreq, 0.1,
			\panmul, 0.1
		)
	])
);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 7

~sections[6] = Pfindur(~durations[6],
	Ppar([
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
		),
		Pbind(
			\instrument, \fshift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[6]/Prand([1,3,5,7],inf),
			\lagdur, Pkey(\dur),
			\amp, Pexprand(0.75,1.0,inf),
			\pan, Pwhite(-1.0,1.0,inf),
			\freq, (Prand(~primes,inf).midicps)*
			Prand([1,3,5,7,11],inf)
		),
		Pbind(
			\instrument, \delay_shift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[6]/Prand([1,3,5,7],inf),
			\amp, Pexprand(0.5,1.0,inf),
			\pan, Pwhite(-1.0,1.0,inf)
		),
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~durations[6],
			\amp, Pexprand(0.5,1.0,inf),
			\bufnum, ~buf_2,
			\rate, Pfunc({((~durations[4]/~durations[6]));}),
			\pan, Pwhite(1.0,-1.0,inf)
		),
		Pbind(
			\instrument, \play_buf,
			\dur, ~durations[6],
			\group, ~fx,
			\out, ~master_fx_bus,
			\amp, Pexprand(0.5,1.0,inf),
			\bufnum, ~buf_1,
			\rate, Pfunc({((~durations[0]/~durations[6])*1.1);}),
			\pan, Pwhite(0.5,-0.5,inf)
		),
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~durations[6],
			\amp, Pexprand(0.5,1.0,inf),
			\bufnum, ~buf_1,
			\rate, Pfunc({((~durations[0]/~durations[6])*1.3);}),
			\pan, Pwhite(0.5,-0.5,inf)
		),
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~durations[6],
			\amp, Pexprand(0.5,1.0,inf),
			\bufnum, ~buf_1,
			\rate, Pfunc({((~durations[0]/~durations[6])*1.5);}),
			\pan, Pwhite(0.5,-0.5,inf)
		),
		Pbind(
			\instrument, \play_buf,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~durations[6],
			\amp, Pexprand(0.5,1.0,inf),
			\bufnum, ~buf_2,
			\rate, Pfunc({((~durations[4]/~durations[6])*1.7);}),
			\pan, Pwhite(0.5,-0.5,inf)
		),
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~durations[6],
			\amp, 1.0,
			\panfreq, 0.25,
			\panmul, 0.75
		)
	])
);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 8

~sections[7] = Pfindur(~durations[7],
	Ppar([
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
		),
		Pbind(
			\instrument, \delay_verb,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\fb, 0.6,
			\dur, ~durations[7]+(~durations[7]*8/10),
			\amp, 0.75
		),
		// this is actually not in the original version... ?
		/*
		Pbind(
		\instrument, \ringmodulator,
		\group, ~fx,
		\in, ~master_fx_bus,
		\out, ~master_fx_bus,
		\dur, ~durations[7]/Prand([1,3,5,7],inf),
		\amp, Pexprand(0.5,1.0,inf),
		\pan, Pwhite(-0.25,0.25,inf),
		\freq, 30.midicps
		),
		*/
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~durations[7],
			\amp, 1.0,
			\panfreq, 0.1,
			\panmul, 0.25
		)
	])
);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 9

~sections[8] = Pfindur(~durations[8],
	Ppar([
		Pbind(
			\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
			\chan, 0,
			\midinote, 	Prand([79],inf)+Pstutter(Prand([3,5,7],inf),Prand(Scale.phrygian.degrees,inf)),
			\dur, Prand([Pn(1/3,3),Pn(1/5,5),1,Pn(1/2,2)],inf)*2,
			\amp, Pexprand(0.6,0.75,inf)
		),
		Pbind(
			\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
			\chan, 0,
			\midinote, 	Prand([79],inf)+Pstutter(Prand([3,5,7],inf),Prand(Scale.phrygian.degrees,inf)),
			\dur, Prand([Pn(1/4,4),Pn(1/6,6),1,Pn(1/2,2)],inf)*2,
			\amp, Pexprand(0.6,0.75,inf)
		),
		Pbind(
			\instrument, \delay_verb,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\fb, 0.5,
			\dur, ~durations[8]+(~durations[8]*8/10),
			\amp, 0.8
		),
		// not in original
		/*
		Pbind(
		\instrument, \ringmodulator,
		\group, ~fx,
		\in, ~master_fx_bus,
		\out, ~master_fx_bus,
		\dur, ~durations[8]/Prand([1,3,5,7],inf),
		\amp, Pexprand(0.5,0.75,inf),
		\freq, Prand([79,79-12,79-24,79-48],inf).midicps
		),
		*/
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~durations[8],
			\amp, 1.0,
			\panfreq, 0,
			\panmul, 0.25
		)
	])
);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 10

~sections[9] = Pfindur(~durations[9],
	Ppar([
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
		),
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
		),
		Pbind(
			\instrument, \quick_delay,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[9]+(~durations[9]*8/10),
			\amp, 1.0,
			\delaytime, Prand([1,2,3,5,7],inf),
			\degree, Prand([-1,-2,-3,-5,-7,1,2,3,5,7],inf).dup(4) // 4 of these
		),
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~durations[9],
			\amp, 1.0,
			\panfreq, 0.2,
			\panmul, 0.17
		)
	])
);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 11

~sections[10] = Pfindur(~durations[10],
	Ppar([
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
		),
		Pbind(
			\instrument, \quick_delay,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[10]+(~durations[10]*8/10),
			\amp, 1.0,
			\delaytime, Prand([1,2,3,5,7],inf),
			\degree, Prand([-1,-2,-3,-5,-7,1,2,3,5,7],inf).dup(4) // 4 of these
		),
		Pbind(
			\instrument, \fshift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, (~durations[10]/(Prand([5,7,11,13,17],inf)))+(~durations[10]*8/10),
			\amp, 1.0,
			\lagdur, Prand([1,2,3,5,7],inf),
			\freq, 79.midicps/Prand([-1,-2,-3,-5,-7,1,2,3,5,7],inf).dup(2) // 2 of these
		),
		Pbind(
			\instrument, \rec_buf,
			\group, ~fx,
			\in, ~master_dry_bus,
			\dur, ~durations[10],
			\amp, 1.0,
			\bufnum, ~buf_1
		),
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~durations[10],
			\amp, 1.0,
			\panfreq, 0.1,
			\panmul, 0.4
		)
	])
);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 12

~sections[11] = Pfindur(~durations[11],
	Ppar([
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
		),
		Pbind(
			\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
			\chan, 0,
			\midinote, Prand([58,62,63,64,67,68,69,70,75],inf),
			\dur, Pxrand([1,2,3,1/2,1/4,1/8],inf),
			\amp, Pexprand(0.25,8,inf)
		),
		Pbind(
			\instrument, \quick_delay_s,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[4]+(~durations[4]*8/10),
			\amp, Pexprand(0.4,0.75),
			\delaytime, Prand([1,2,3,5,7],inf).dup(8), // 8 of these
			\degree, Prand([-1,-2,-3,-5,-7,1,2,3,5,7],inf),
			\freq, Pkey(\delaytime)/10,
			\mul, Pseries(0,Pexprand(0.1,0.5),inf)
		),
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~durations[11],
			\amp, 1.0,
			\panfreq, 0.2,
			\panmul, 0.2
		)
	])
);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 13

~sections[12] = Pfindur(~durations[12],
	Ppar([
		Pbind(
			\type, \midi,\midicmd, \noteOn, \midiout, ~mOut,
			\chan, 0,
			\midinote, Prand([58,62,63,64,67,68,69,70,75],inf),
			\dur, Pseq([1,2,3],inf)/Pseq([Pn(2,4),Pn(3,3),5,5,7,7,11,11,13,17],inf),
			\amp, Pexprand(0.25,8,inf)
		),
		Pbind(
			\instrument, \quick_delay,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[12]/Prand([1,3,5,7]),
			\amp, 1.0,
			\delaytime, Prand([1,2,3,5,7],inf),
			\degree, Prand([-1,-2,-3,-5,-7,1,2,3,5,7],inf).dup(4) // 4 of these
		),
		Pbind(
			\instrument, \play_buf_d,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~durations[12],
			\amp, Pexprand(0.7,1.0,inf),
			\degree, Prand([3,5,7,-2,-4,-6,-8,-10,-12],inf).dup(8) // 8 of these
		),
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~durations[12],
			\amp, 0.5,
			\panfreq, 0.2,
			\panmul, 0.7
		)
	])
);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 14

~section13_piano1 = Pbind(
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
);

~sections[13] = Pfindur(~durations[13],
	Ptpar([
		10, ~section13_piano1,
		10, ~section13_piano1,
		10, ~section13_piano1,
		10, ~section13_piano1,
		10,
		Pbind(
			\instrument, \rec_buf,
			\group, ~fx,
			\in, ~master_dry_bus,
			\dur, ~durations[13],
			\amp, 1.0
		),
		10,
		Pbind(
			\instrument, \delay_shift,
			\group, ~fx,
			\in, ~master_fx_bus,
			\out, ~master_fx_bus,
			\dur, ~durations[13]+(~durations[13]*8/10),
			\amp, Pexprand(0.5,1.0)
			//\dtime_array, #[1, 3, 5, 7, 9, 11, 13]*2
		),
		10,
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~durations[13],
			\amp, 1.0,
			\panfreq, 0,
			\panmul, 0.25
		)
	])
);

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 15

~sections[14] = Pfindur(~durations[14]+(~durations[14]/2),
	Ppar([
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
		),
		Pbind(
			\instrument, \play_buf_d,
			\group, ~fx,
			\out, ~master_fx_bus,
			\dur, ~durations[14],
			\amp, 1.0,
			\degree, Prand([-11,-7,-5,-2,-1,1,2,3,5,7,11],inf).dup(6) // 6 of these
		),
		Pbind(
			\instrument, \panner,
			\group, ~pan,
			\in, ~master_fx_bus,
			\out, ~master_pan_bus,
			\dur, ~durations[14],
			\amp, 1.0,
			\panfreq, 0,
			\panmul, 0.25
		)
	])
);

)