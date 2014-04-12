/************************************	*/
/* Untitled Piece		             	*/
/*	PATTERNS      			    		*/
/************************************	*/



(
~load_patterns = nil;

// init sections arrays
~numsections = 100;
~sections = 0!~numsections;
~durations = 8!~numsections;
~delays = 0!~numsections;

// set durations for each section in this file
~durations[0] = 70; ~delays[0] = 0;

// any global functions or systems/scales relating to the music should go here
~hseries =
Array.fill(8, { |i| if(i>0,{i=i*4.rand.max(1)});
	Array.fill(64,{|j| j=j+1;
		j * Array.fill(100,{|n| n=n+1; n*26.midicps})[i+(j-1)];
	}).cpsmidi;
});

////////////////////////////////////////////////////////////////////////////////
~sections[0] = Pfindur(~durations[0],
	Ptpar([
		// program changes
		~delays[0],
		Pbind(
			\type, \ctevent,
			\oscout, ~osc_destination,
			\osccmd, \program,
			\voicename, [\vi1, \vi2, \va, \vc],
			\programname,
			#[
				"violin.ordinario",
				"violin.ordinario",
				"viola.ordinario",
				"violoncello.ordinario"
			],
			\dur, Pn(0.01,1)
		),
		// violin 1
		~delays[0]+0.05,
		Pbind(
			\type, \ctevent,
			\oscout, ~osc_destination,
			\osccmd, \noteon,
			\voicename, \vi1,
			\midinote, 49,
			\dur, 16,
			\amp, Pexprand(0.75,1.0,inf)
		),
		// violin 2
		~delays[0]+0.05,
		Pbind(
			\type, \ctevent,
			\oscout, ~osc_destination,
			\osccmd, \noteon,
			\voicename, \vi2,
			\midinote, 45,
			\dur, 16,
			\amp, Pexprand(0.75,1.0,inf)
		),
		// viola
		~delays[0]+0.05,
		Pbind(
			\type, \ctevent,
			\oscout, ~osc_destination,
			\osccmd, \noteon,
			\voicename, \va,
			\midinote, 42,
			\dur, 16,
			\amp, Pexprand(0.1,0.7,inf)
		),
		// violoncello
		~delays[0]+0.05,
		Pbind(
			\type, \ctevent,
			\oscout, ~osc_destination,
			\osccmd, \noteon,
			\voicename, \vc,
			\midinote, 30,
			\dur, 16,
			\amp, Pexprand(0.1,0.7,inf)
		)
	], 1)
);
)