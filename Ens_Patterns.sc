/************************************	*/
/* Ensemble Piece		             	*/
/*	PATTERNS						*/
/************************************	*/



(
//TempoClock.default.tempo = 1.0;
~load_patterns = nil;
~scales = 0!32;
~sections = 0!32;
~durations = 8!32;

~durations[0] = 60;
~durations[1] = 50;
~durations[2] = 50;

~pelog = Array.fill(10, {|i| Scale.pelog.degrees + (12*i) }).lace.sort;
	
~load_patterns = {
	////////////////////////////////////////////////////////////////////////////////
	~sections[0] = Ptpar([
		// first do program changes to initiate
		0,
		Pdef(\section0_pg, 
			Pbind(
				\type, \ctosc, 
				\oscout, ~osc_destination,
				\osccmd, \program,
				\voicename, [\fl,\bfl,\bcl,\va1],
				\programname, 
					#["flute.air noise.closed.vowel varied",
					"bass.flute.multiphonic",
					"bass clarinet boehm system.ordinario",
					"viola.harmonic.artificial.fourth"],
				\dur, Pn(0.01,1)
			)	
			 
		),
		0.05,
		Pdef(\section0_fl, 
			Pfindur(~durations[0], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, \fl,
					\midinote, 69+Pseq([0,2],inf),
					\dur, Pseq([16,8,8,8],inf),
					\legato, 0.75,
					\amp, Pexprand(0.75,1.0,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section0_bfl, 
			Pfindur(~durations[0], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, \bfl,
					\midinote, 81+Pseq([0,2,3],inf), 
					\dur, Pseq([16,8,8,4],inf),
					\amp, Pexprand(0.1,0.25,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section0_bcl, 
			Pfindur(~durations[0], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\rest,Pwrand([\rest,\noteon],[0.25,0.75],inf)],1),
					\voicename, \bcl,
					\midinote, 81, 
					\dur, Pseq([8,Prand([4,8,16],inf)],inf),
					\amp, Pexprand(0.1,0.25,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section0_va1, 
			Pfindur(~durations[0], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\rest,Prand([\rest,\noteon],inf)],1),
					\voicename, \va1,
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=85}),inf), 							\dur, Pseq([Pn(16,1),Prand([8,16,32],inf)],1),
					\amp, Pexprand(0.1,0.7,inf)
				)	
			) 
		)
	], 1);
	
	////////////////////////////////////////////////////////////////////////////////
	~sections[1] = Ptpar([
		0,
		Pdef(\section1_pg, 
			Pbind(
				\type, \ctosc, 
				\oscout, ~osc_destination,
				\osccmd, \program,
				\voicename, [\bfl,\bcl,\sx1,\vi1,\vi2,\va1,\va2],
				\programname, 
					#["bass.flute.jet whistle",
					"bass clarinet boehm system.multiphonic.Vilhjalmsson Buffet",
					"alto saxophone.slap.percussive slap",
					"violin.harmonic.artificial.fourth",
					"violin.harmonic.artificial.fourth",
					"viola.harmonic.artificial.fourth",
					"viola.harmonic.artificial.fourth"],
				\dur, Pn(0.01,1)
			)	
			 
		),
		0.05,
		Pdef(\section1_fl, 
			Pfindur(~durations[1], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, \fl,
					\midinote, 69+Pseq([0,2],inf),
					\dur, Pseq([16,8,8,8],inf),
					\legato, 0.75,
					\amp, Pexprand(0.75,1.0,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section1_bfl, 
			Pfindur(~durations[1], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Prand([\noteon,\rest],inf),
					\voicename, \bfl,
					\midinote, Prand([49,52,53,56,59,62],inf), 
					\dur, Prand([16,8,32],inf),
					\amp, Pexprand(0.1,0.25,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section1_bcl, 
			Pfindur(~durations[1], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pwrand([\rest,\noteon],[0.5,0.5],inf),
					\voicename, \bcl,
					\midinote, Pseq([121,122],inf), 
					\dur, Pseq([8,Prand([4,8,16],inf)],inf),
					\amp, Pexprand(0.1,0.25,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section1_sx1, 
			Pfindur(~durations[1], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\rest,Pn(\noteon,inf)],1),
					\voicename, \sx1,
					\midinote, Pseq([50,51],32), 
					\dur, Pseq([8,Pn(1/16,32)],inf),
					\amp, Pseq([0,Pseg(Pseq([0.2,1,0.0],1),Pseq(1!2,1))],1)
				)	
			) 
		),
		0.05,
		Pdef(\section1_tam, 
			Pfindur(~durations[1], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, \tam,
					\midinote, 67, 
					\dur, Pn(16,1),
					\amp, Pexprand(0.5,1.0,1)
				)	
			) 
		),
		0.05,
		Pdef(\section1_vi1, 
			Pfindur(~durations[1], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Prand([\rest,\noteon],inf),
					\voicename, \vi1,
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=81}),inf), 							\dur, Pseq([Pn(6,1),Prand([2,8,16],inf)],1),
					\amp, Pexprand(0.1,0.6,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section1_vi2, 
			Pfindur(~durations[1], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Prand([\rest,\noteon],inf),
					\voicename, \vi2,
					\midinote, 											Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=81}),inf), 
					\dur, Pseq([Pn(6,1),Prand([2,8,16],inf)],1),
					\amp, Pexprand(0.1,0.6,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section1_va1, 
			Pfindur(~durations[1], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Prand([\rest,\noteon],inf),
					\voicename, \va1,
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=73}),inf), 							\dur, Pseq([Pn(6,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.1,0.6,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section1_va2, 
			Pfindur(~durations[1], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Prand([\rest,\noteon],inf),
					\voicename, \va2,
					\midinote, 											Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=73}),inf), 
					\dur, Pseq([Pn(6,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.1,0.6,inf)
				)	
			) 
		)
	], 1);

	////////////////////////////////////////////////////////////////////////////////
	~sections[2] = Ptpar([
		0,
		Pdef(\section2_pg, 
			Pbind(
				\type, \ctosc, 
				\oscout, ~osc_destination,
				\osccmd, \program,
				\voicename, [\bfl,\sx1,\sx2,\cb,\vc],
				\programname, 
					#["bass.flute.ordinario",
					"alto saxophone.slap.percussive slap",
					"alto saxophone.multiphonic.Gubler Selmer_Super_Action_II",
					"double bass.pizzicato.bartok",
					"violoncello.flautando"],
				\dur, Pn(0.01,1)
			)	
			 
		),
		0.05,
		Pdef(\section2_bfl, 
			Pfindur(~durations[2], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \bfl,
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=48}).select({|n,i| n<=84}),inf), 					\dur, Pseq([Pn(16,1),Prand([2,8,16],inf)],1),
					\amp, Pexprand(0.7,1.0,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section2_sx1, 
			Pfindur(~durations[2], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, \sx1,
					\midinote, Pseq([50,51],32), 
					\dur, Pseq([8,Pn(1/16,64)],inf),
					\amp, Pseq([0,Pseg(Pseq([0.2,1,0.0],1),Pseq(2!2,1))],1)
				)	
			) 
		),
		0.05,
		Pdef(\section2_sx2, 
			Pfindur(~durations[2], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\rest,Pn(\noteon,inf)],1),
					\voicename, \sx2,
					\midinote, Pseq([0,107],1), 
					\dur, Pseq([16,Pn(16,inf)],inf),
					\amp, Pseq([0,Pseg(Pseq([0.2,1,0.0],1),Pseq(2!2,1))],1)
				)	
			) 
		),
		0.05,
		Pdef(\section2_cb, 
			Pfindur(~durations[2], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, \cb,
					\midinote, Pn(38,inf), 
					\dur, Pseq([16,Prand([32,16,8],inf)],inf),
					\amp, Pexprand(0.85,1.0,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section2_vi1, 
			Pfindur(~durations[2], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \vi1,
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=81}),inf), 							\dur, Pseq([Pn(16,1),Prand([2,8,16],inf)],1),
					\amp, Pexprand(0.7,1.0,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section2_vi2, 
			Pfindur(~durations[2], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \vi2,
					\midinote, 											Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=81}),inf), 
					\dur, Pseq([Pn(16,1),Prand([2,8,16],inf)],1),
					\amp, Pexprand(0.7,1.0,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section2_vi3, 
			Pfindur(~durations[2], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \vi1,
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=81}),inf), 							\dur, Pseq([Pn(16,1),Prand([2,8,16],inf)],1),
					\amp, Pexprand(0.7,1.0,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section2_vi4, 
			Pfindur(~durations[2], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \vi2,
					\midinote, 											Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=81}),inf), 
					\dur, Pseq([Pn(16,1),Prand([2,8,16],inf)],1),
					\amp, Pexprand(0.7,1.0,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section2_va1, 
			Pfindur(~durations[2], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \va1,
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=73}),inf), 							\dur, Pseq([Pn(16,1),Prand([2,8,16],inf)],1),
					\amp, Pexprand(0.7,1.0,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section2_va2, 
			Pfindur(~durations[2], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \va2,
					\midinote, 											Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=73}),inf), 
					\dur, Pseq([Pn(16,1),Prand([2,8,16],inf)],1),
					\amp, Pexprand(0.7,1.0,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section2_vc, 
			Pfindur(~durations[2], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \vc,
					\midinote, 											Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n<=84}),inf), 
					\dur, Pseq([Pn(16,1),Prand([2,8,16],inf)],1),
					\amp, Pexprand(0.7,1.0,inf)
				)	
			) 
		)

	], 1);	
};
~load_patterns.fork;
)