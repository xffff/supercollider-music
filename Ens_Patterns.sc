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
	~sections[0] = Ppar([
		Pdef(\section0_fl, 
			Pfindur(~durations[0], 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, [\vi1,\va1],
					\midinote, Ptuple([Pseg(Pseq((60..75),inf),5),Pseg(Pseq((60..75),inf),3)],inf),
					\dur, 1/16,
					\amp, Pexprand(0.75,1.0,inf)
				)	
			) 
		)
	], 1);			
};

)