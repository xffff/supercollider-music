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

~pelog = Array.fill(10, {|i| Scale.pelog.degrees + (12*i) }).lace.sort;
	
~load_patterns = fork {
	~sections[0] = Ptpar([
		// first do program changes to initiate
		0,
		Pdef(\section0_pg, 
			Pbind(
				\type, \ctosc, 
				\oscout, ~osc_destination,
				\osccmd, \program,
				\voicename, [\fl,\bfl],
				\programname, 
					#["flute.air noise.closed.vowel.varied",
					"bass.flute.multiphonic"],
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
					\osccmd, \noteon,
					\voicename, \bcl,
					\midinote, 81, 
					\dur, Pn(~durations[0],1),
					\amp, Pexprand(0.1,0.25,inf)
				)	
			) 
		)
	], 1);
	
	~sections[1] = Ptpar([
		0,
		Pdef(\section1_pg, 
			Pbind(
				\type, \ctosc, 
				\oscout, ~osc_destination,
				\osccmd, \program,
				\voicename, [\sx1,\vi1,\vi2],
				\programname, 
					#["alto saxophone.multiphonic.Gubler Selmer_Super_Action_II",
					"violin.harmonic.artificial.fourth",
					"violin.harmonic.artificial.fourth"],
				\dur, Pn(0.01,1)
			)	
			 
		),
		0.05,
		Pdef(\section1_sx1, 
			Pfindur(~durations[0], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, \sx1,
					\midinote, 105, 
					\dur, Pn(8,1),
					\amp, Pexprand(0.1,0.5,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section1_vi1, 
			Pfindur(~durations[0], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\rest,Pn(\noteon,1)],1),
					\voicename, \vi1,
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=81}),inf), 					\dur, Pseq([2,8],inf),
					\amp, Pexprand(0.1,0.5,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section1_vi2, 
			Pfindur(~durations[0], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\rest,Pn(\noteon,1)],1),
					\voicename, \vi2,
					\midinote, 											Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=81}),inf), 
					\dur, Pseq([2,8],inf),
					\amp, Pexprand(0.1,0.25,inf)
				)	
			) 
		)
	], 1)			
};

)