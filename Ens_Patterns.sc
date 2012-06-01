/************************************	*/
/* Ensemble Piece		             	*/
/*	PATTERNS						*/
/************************************	*/



(
//TempoClock.default.tempo = 1.0;
~load_patterns = nil;
~scales = 0!32;
~sections = 0!32;
~durations = 60!32;

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
				\voicename, [\bfl,\vc,\vi],
				\programname, 
					["bass flute.multiphonic",
					"violin.harmonic.artificial.fourth",
					"violoncello.flautando"]
				\dur, 0.01
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
					\degree, Prand(Scale.lydian.degrees,inf),
					\octave, 5,
					\dur, Prand([1/4,1/2,1],inf),
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
					\degree, Prand(Scale.lydian.degrees,inf),
					\octave, 3,
					\dur, Prand([1/4,1/2,1],inf),
					\amp, Pexprand(0.75,1.0,inf)
				)	
			) 
		)
	], 1);			
};

)