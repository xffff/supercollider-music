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
				\voicename, [\bfl,\vi1,\va1,\vc],
				\programname, 
					#["bass.flute.multiphonic",
					"violin.harmonic.artificial.fourth",
					"viola.harmonic.artificial.fourth",
					"violoncello.flautando"],
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
					\midinote, 93+Pseq([2,3,0],inf),
					\dur, Pseq([8,4,4],inf),
					\amp, Pexprand(0.1,0.2,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section0_bclbfl, 
			Pfindur(~durations[0], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, [\bcl,\bfl],
					\midinote, 81+Pseq([2,3,0],inf), 
					\dur, Pseq([8,4,4],inf),
					\amp, Pexprand(0.1,0.25,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section0_vi1va, 
			Pfindur(~durations[0], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, [\vi1,\va1],
					\midinote, 96+[0,4,7]+Pseq([2,3,0],inf),
					\dur,  Pseq([8,4,4],inf),
					\amp, Pexprand(0.25,0.5,inf)
				)	
			) 
		),
		0.05,
		Pdef(\section0_vc, 
			Pfindur(~durations[0], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, \vc,
					\midinote, 57+Pseq([2,3,0],inf),
					\dur, Pseq([8,4,4],inf),
					\amp, Pexprand(0.25,0.5,inf)
				)	
			) 
		)


	], 1);			
};

)