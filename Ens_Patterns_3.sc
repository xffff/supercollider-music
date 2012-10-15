/************************************	*/
/* Ensemble Piece		             	*/
/*	PATTERNS	13-					*/
/************************************	*/


(
~durations[13] = 128; ~delays[13] = 0;
~durations[14] = 82; ~delays[14] = 0;
~durations[15] = 82; ~delays[15] = 0;

//////////////////////////////////////////////////////////////////////////////////
~sections[13] = Pfindur(~durations[13],
	Ptpar([
	// program changes
		0, // give some delay because of the massive amount of prog changes
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \program,
			\voicename, [\fl,\bfl,\bcl,\bsn1,\bsn2,\tr1,\tr2,\tb1,\tb2,
						\sx1,\sx2,\vi1,\vi2,\vi3,\vi4,\va1,\va2,\vc,\cb],
			\programname, 
				#["flute.ordinario",
				"bass.flute.ordinario",
				"bass clarinet boehm system.ordinario",
				"bassoon.ordinario.Schawrz Heckel",
				"bassoon.ordinario.Schawrz Heckel",
				"trumpet in c.ordinario",
				"trumpet in c.ordinario",		
				"tenor trombone.ordinario",
				"tenor trombone.ordinario",
				"alto saxophone.ordinario",
				"alto saxophone.ordinario",
				"violin.ordinario",
				"violin.ordinario",
				"violin.ordinario",	
				"violin.ordinario",	
				"viola.ordinario",
				"viola.ordinario",
				"violoncello.ordinario",
				"double bass.ordinario"],
			\dur, Pn(0.01,1)
		),
	// flute
		~delays[13]+0.025,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\detuned_noteon,Prand([\rest,\detuned_noteon],inf)],1),
			\voicename, \fl,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=59}).select({|n,i| n<=96}),Prand((1..4),1)), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=59}).select({|n,i| n<=96}),Prand((1..4),1)), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=59}).select({|n,i| n<=96}),Prand((1..4),1)), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=59}).select({|n,i| n<=96}),Prand((1..4),1)), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=59}).select({|n,i| n<=96}),Prand((1..4),1)), 
				],inf),
			\dur, Pseq([16,Prand([16,8,4],inf)],1).collect({|dur| ~wwdur = dur; dur}),
			\legato, 0.99,
			\amp, Pexprand(0.1,1.0,inf)
		),
	// bass flute
		~delays[13]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\detuned_noteon,Prand([\rest,\detuned_noteon],inf)],1),
			\voicename, \bfl,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=48}).select({|n,i| n<=84}),Prand((1..4),1)), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=48}).select({|n,i| n<=84}),Prand((1..4),1)), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=48}).select({|n,i| n<=84}),Prand((1..4),1)), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=48}).select({|n,i| n<=84}),Prand((1..4),1)), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=48}).select({|n,i| n<=84}),Prand((1..4),1)), 
				],inf),
			\dur, Pfunc({~wwdur}),
			\amp, Pexprand(0.1,1.0,inf)
		),
	// bass clarinet
		~delays[13]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\detuned_noteon,Prand([\rest,\detuned_noteon],inf)],1),
			\voicename, \bcl,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=34}).select({|n,i| n<=85}),Prand((1..4),1)), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=34}).select({|n,i| n<=85}),Prand((1..4),1)), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=34}).select({|n,i| n<=85}),Prand((1..4),1)), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=34}).select({|n,i| n<=85}),Prand((1..4),1)), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=34}).select({|n,i| n<=85}),Prand((1..4),1)), 
				],inf),
			\dur, Pfunc({~wwdur}),
			\legato, 0.99,		
			\amp, Pexprand(0.1,1.0,inf)
		),
	// bassoon 1
		~delays[13]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\detuned_noteon,Prand([\rest,\detuned_noteon],inf)],1),
			\voicename, \bsn1,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),Prand((1..4),1)), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),Prand((1..4),1)), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),Prand((1..4),1)), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),Prand((1..4),1)), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),Prand((1..4),1)), 
				],inf),
			\dur, Pfunc({~wwdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.1,1.0,inf)
		),
	// bassoon 2
		~delays[13]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\detuned_noteon,Prand([\rest,\detuned_noteon],inf)],1),
			\voicename, \bsn2,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),Prand((1..4),1)), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),Prand((1..4),1)), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),Prand((1..4),1)), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),Prand((1..4),1)), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),Prand((1..4),1)), 
				],inf),
			\dur, Pfunc({~wwdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.1,1.0,inf)
		),	
	// trumpet 1
		~delays[13]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\detuned_noteon,Prand([\rest,\detuned_noteon],inf)],1),
			\voicename, \tr1,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=24}).select({|n,i| n<=84}),Prand((1..4),1)), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=24}).select({|n,i| n<=84}),Prand((1..4),1)), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=24}).select({|n,i| n<=84}),Prand((1..4),1)), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=24}).select({|n,i| n<=84}),Prand((1..4),1)), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=24}).select({|n,i| n<=84}),Prand((1..4),1)), 
				],inf),
			\dur, Pseq([16,Prand([16,8,4],inf)],1).collect({|dur| ~brassdur = dur; dur}),
			\legato, 0.99,					
			\amp, Pexprand(0.1,0.75,inf)
		),
	// trumpet 2
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\detuned_noteon,Prand([\rest,\detuned_noteon],inf)],1),
			\voicename, \tr2,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=24}).select({|n,i| n<=84}),Prand((1..4),1)), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=24}).select({|n,i| n<=84}),Prand((1..4),1)), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=24}).select({|n,i| n<=84}),Prand((1..4),1)), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=24}).select({|n,i| n<=84}),Prand((1..4),1)), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=24}).select({|n,i| n<=84}),Prand((1..4),1)), 
				],inf),
			\dur, Pfunc({~brassdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.1,0.75,inf)
		),		
	// trombone 1
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\detuned_noteon,Prand([\rest,\detuned_noteon],inf)],1),
			\voicename, \tb1,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=30}).select({|n,i| n<=80}),Prand((1..4),1)), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=30}).select({|n,i| n<=80}),Prand((1..4),1)), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=30}).select({|n,i| n<=80}),Prand((1..4),1)), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=30}).select({|n,i| n<=80}),Prand((1..4),1)), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=30}).select({|n,i| n<=80}),Prand((1..4),1)), 
				],inf),
			\dur, Pfunc({~brassdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.1,0.75,inf)
		),
	// trombone 2
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\detuned_noteon,Prand([\rest,\detuned_noteon],inf)],1),
			\voicename, \tb2,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=30}).select({|n,i| n<=80}),Prand((1..4),1)), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=30}).select({|n,i| n<=80}),Prand((1..4),1)), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=30}).select({|n,i| n<=80}),Prand((1..4),1)), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=30}).select({|n,i| n<=80}),Prand((1..4),1)), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=30}).select({|n,i| n<=80}),Prand((1..4),1)), 
				],inf),
			\dur, Pfunc({~brassdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.1,0.75,inf)
		),		
	// saxophone 1
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\detuned_noteon,Prand([\rest,\detuned_noteon],inf)],1),
			\voicename, \sx1,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),Prand((1..4),1)), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),Prand((1..4),1)), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),Prand((1..4),1)), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),Prand((1..4),1)), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),Prand((1..4),1)), 
				],inf),
			\dur, Pfunc({~brassdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.1,0.75,inf)
		),
	// saxophone 2
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\detuned_noteon,Prand([\rest,\detuned_noteon],inf)],1),
			\voicename, \sx2,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),Prand((1..4),1)), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),Prand((1..4),1)), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),Prand((1..4),1)), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),Prand((1..4),1)), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),Prand((1..4),1)), 
				],inf),
			\dur, Pfunc({~brassdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.1,0.75,inf)
		),
	// violin 1
		~delays[13]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\detuned_noteon,Prand([\rest,\detuned_noteon],inf)],1),
			\voicename, \vi1,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),Prand((1..4),1)), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),Prand((1..4),1)), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),Prand((1..4),1)), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),Prand((1..4),1)), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),Prand((1..4),1)), 
				],inf),
			\dur, Pseq([16,Prand([16,8,4],inf)],1).collect({|dur| ~stringsdur = dur; dur}),
			\legato, 0.99,					
			\amp, Pexprand(0.4,1.0,inf)
		),
	// violin 2
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\detuned_noteon,Prand([\rest,\detuned_noteon],inf)],1),
			\voicename, \vi2,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),Prand((1..4),1)), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),Prand((1..4),1)), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),Prand((1..4),1)), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),Prand((1..4),1)), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),Prand((1..4),1)), 
				],inf),
			\dur, Pfunc({~stringsdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.4,1.0,inf)
		),
	// violin 3
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\detuned_noteon,Prand([\rest,\detuned_noteon],inf)],1),
			\voicename, \vi3,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),Prand((1..4),1)), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),Prand((1..4),1)), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),Prand((1..4),1)), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),Prand((1..4),1)), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),Prand((1..4),1)), 
				],inf),
			\dur, Pfunc({~stringsdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.4,1.0,inf)
		),
	// violin 4
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\detuned_noteon,Prand([\rest,\detuned_noteon],inf)],1),
			\voicename, \vi4,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),Prand((1..4),1)), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),Prand((1..4),1)), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),Prand((1..4),1)), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),Prand((1..4),1)), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),Prand((1..4),1)), 
				],inf),
			\dur, Pfunc({~stringsdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.4,1.0,inf)
		),		
	// viola 1
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\detuned_noteon,Prand([\rest,\detuned_noteon],inf)],1),
			\voicename, \va1,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),Prand((1..4),1)), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),Prand((1..4),1)), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),Prand((1..4),1)), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),Prand((1..4),1)), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),Prand((1..4),1)), 
				],inf),
			\dur, Pfunc({~stringsdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.4,1.0,inf)
		),
	// viola 2
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\detuned_noteon,Prand([\rest,\detuned_noteon],inf)],1),
			\voicename, \va2,
			\midinote, 	
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),Prand((1..4),1)), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),Prand((1..4),1)), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),Prand((1..4),1)), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),Prand((1..4),1)), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),Prand((1..4),1)), 
				],inf),
			\dur, Pfunc({~stringsdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.4,1.0,inf)
		),
	// violoncello
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\detuned_noteon,Prand([\rest,\detuned_noteon],inf)],1),
			\voicename, \vc,
			\midinote, Pseq([
				~hseries[0][1],
				~hseries[1][2],
				~hseries[2][3],
				~hseries[3][4],
				~hseries[4][5]],inf), 
			\dur, Pseq([16,Prand([16,8,4],inf)],1).collect({|dur| ~bass = dur; dur}),
			\legato, 0.99,					
			\amp, Pexprand(0.4,1.0,inf)
		),
	// contrabass
		~delays[13]+0.08,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \detuned_noteon,
			\voicename, \cb,
			\midinote, Pseq([
				~hseries[0][0],
				~hseries[1][0],
				~hseries[2][0],
				~hseries[3][0],
				~hseries[4][0]],inf),
			\dur, Pfunc({~bass}),
			\legato, 0.99,					
			\amp, Pexprand(0.4,1.0,inf)
		),
	// crotales			
		~delays[13]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \detuned_noteon,
			\voicename, \ctl,
			\midinote, 
				Pwalk(
					union(
						union(
							union(
								union(~hseries[0],~hseries[1]),
							~hseries[2]),
						~hseries[3]),
					~hseries[4]).select({|n,i| 
						n>=72}).select({|n,i| n<=96}),
					Prand([-2,-1,1,2],inf),
					Pseq([-1,1],inf),
					0),
			\dur, 
				Pseq([Pn(1/4,4),
					Pwrand([16,Pn(1/8,8),Pn(1/4,16),Pn(1/7,7),Pn(1/3,3),8,3,2,1,Pn(4,2)],
						{1.0.rand}.dup(10).normalizeSum,inf)],inf),
			\legato, 2,
			\amp, Pexprand(0.7,1.0,inf)
		),		
	// crotales -> warp
		~delays[13]+0.075,
		Pbind(
			\instrument, \warp,
			\group, ~fx,
			\in, ~master_dry_bus.subBus(12,1),
			\out, ~master_fx_bus.subBus(12,1),
			\dur, ~durations[13],
			\atk, ~durations[13] * 0.1,
			\sus, ~durations[13] * 0.1,
			\rel, ~durations[13] * 0.8, // slight overlap with s2
			\amp, 1.0,
			\warpfactor, [-36,-24,-12,-7].midiratio,
			\freqscale, Pkey(\warpfactor)
		),
	// crotales -> hala
		~delays[13]+0.05,
		PmonoArtic(
			\hala,
			\group, ~hala,
			\in, ~master_fx_bus.subBus(12,1),
			\out, ~master_mix_bus.subBus(26,6), 
			\dur, ~durations[13] / 381,
			\atk, ~durations[13] * 0.01,
			\sus, ~durations[13] * 0.99,
			\rel, ~durations[13] * 0.01, 
			\amp, 1.0,
			\pan, Pbrown(-1.0,1.0,0.05,inf),
			\legato, 1.1
		),				
	// tam-tam
		~delays[13]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \detuned_noteon,
			\voicename, \tam,
			\midinote, Prand((62..64),1), 
			\dur, Pn(64,1),
			\amp, Pexprand(0.7,1.0,1)		
		),
	// bass drum
		~delays[13]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pn(\detuned_noteon,inf),
			\voicename, \bd,
			\midinote, Prand([61,62],inf),
			\dur, 32,
			\amp, Pexprand(0.7,1.0,1)		
		)				
	], 1),
0);

//////////////////////////////////////////////////////////////////////////////////

~sections[14] = Pfindur(~durations[14],
	Ptpar([
	// program changes
		0, // give some delay because of the massive amount of prog changes
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \program,
			\voicename, [\vi1,\vi2,\vi3,\vi4,\va1,\va2,\vc,\cb],
			\programname, 
				#["violin.ordinario",
				"violin.ordinario",
				"violin.ordinario",	
				"violin.ordinario",	
				"viola.ordinario",
				"viola.ordinario",
				"violoncello.ordinario",
				"double bass.ordinario"],
			\dur, Pn(0.01,1)
		),
	// Saw
		~delays[14]+0.05,
		Pbind(
			\instrument, \saw,
			\group, ~input,
			\out, [~master_dry_bus.subBus(15,1),15],
			\atk, ~durations[14],
			\sus, 0.001,
			\rel, 20,
			\freq, 26.midicps,
			\dur, ~durations[14],
			\amp, 0.3
		),
	// saw -> reverb
		~delays[14]+0.05,
		Pbind(
			\instrument, \gverb,
			\group, ~fx,
			\in, ~master_dry_bus.subBus(15,1),
			\out, ~master_mix_bus.subBus(24,1),
			\dur, ~durations[14],
			\atk, 0.01,
			\sus, ~durations[14],
			\rel, 40, 
			\amp, 1.0,
			\roomsize, 243, 
			\revtime, 15, 
			\damping, 0.3, 
			\inputbw, 0.34, 
			\drylevel, -80, 
			\earlylevel, -11, 
			\taillevel, -9
		),								
	// violin 1
		~delays[14]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \detuned_noteon,
			\voicename, \vi1,
			\midinote, Pseq([
				~hseries[0][7],
				~hseries[1][7],
				~hseries[2][7],
				~hseries[3][7],
				~hseries[4][7]],inf), 
			\dur, Pn(16,inf).collect({|dur| ~stringsdur = dur; dur}),
			\legato, 0.99*Pseq([1],inf).collect({|dur| ~stringsleg = dur; dur}),
			\amp, Pexprand(0.6,1.0,inf)
		),
	// violin 2
		~delays[14]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \detuned_noteon,
			\voicename, \vi2,
			\midinote, Pseq([
				~hseries[0][6],
				~hseries[1][6],
				~hseries[2][6],
				~hseries[3][6],
				~hseries[4][6]],inf), 
			\dur, Pfunc({~stringsdur}),
			\legato, Pfunc({~stringsleg}),
			\amp, Pexprand(0.6,1.0,inf)
		),
	// violin 3
		~delays[14]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \detuned_noteon,
			\voicename, \vi3,
			\midinote, Pseq([
				~hseries[0][5],
				~hseries[1][5],
				~hseries[2][5],
				~hseries[3][5],
				~hseries[4][5]],inf), 
			\dur, Pfunc({~stringsdur}),
			\legato, Pfunc({~stringsleg}),
			\amp, Pexprand(0.6,1.0,inf)
		),
	// violin 4
		~delays[14]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \detuned_noteon,
			\voicename, \vi4,
			\midinote, Pseq([
				~hseries[0][4],
				~hseries[1][4],
				~hseries[2][4],
				~hseries[3][4],
				~hseries[4][4]],inf), 
			\dur, Pfunc({~stringsdur}),
			\legato, Pfunc({~stringsleg}),
			\amp, Pexprand(0.6,1.0,inf)
		),	
	// viola 1
		~delays[14]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \detuned_noteon,
			\voicename, \va1,
			\midinote, Pseq([
				~hseries[0][3],
				~hseries[1][3],
				~hseries[2][3],
				~hseries[3][3],
				~hseries[4][3]],inf), 
			\dur, Pfunc({~stringsdur}),
			\legato, Pfunc({~stringsleg}),
			\amp, Pexprand(0.6,1.0,inf)
		),
	// viola 2
		~delays[14]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \detuned_noteon,
			\voicename, \va2,
			\midinote, Pseq([
				~hseries[0][2],
				~hseries[1][2],
				~hseries[2][2],
				~hseries[3][2],
				~hseries[4][2]],inf), 
			\dur, Pfunc({~stringsdur}),
			\legato, Pfunc({~stringsleg}),
			\amp, Pexprand(0.6,1.0,inf)
		),
	// violoncello
		~delays[14]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \detuned_noteon,
			\voicename, \vc,
			\midinote, Pseq([
				~hseries[0][1],
				~hseries[1][1],
				~hseries[2][1],
				~hseries[3][1],
				~hseries[4][1]],inf), 
			\dur, Pfunc({~stringsdur}),
			\legato, Pfunc({~stringsleg}),
			\amp, Pexprand(0.6,1.0,inf)
		),
	// contrabass
		~delays[14]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \detuned_noteon,
			\voicename, \cb,
			\midinote, Pseq([
				~hseries[0][0],
				~hseries[1][0],
				~hseries[2][0],
				~hseries[3][0],
				~hseries[4][0]],inf),
			\dur, Pfunc({~stringsdur}),
			\legato, Pfunc({~stringsleg}),
			\amp, Pexprand(0.6,1.0,inf)
		),
	// bass drum
		~delays[14]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Pn(\rest,inf)],1),
			\voicename, \bd,
			\midinote, Prand([61,62],inf),
			\dur, 32,
			\amp, 1
		)							
	], 1),
0);

//////////////////////////////////////////////////////////////////////////////////

~sections[15] = Pfindur(~durations[15],
	Ptpar([
	// program changes
		0, // give some delay because of the massive amount of prog changes
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \program,
			\voicename, [\fl,\bfl,\cb],
			\programname, 
				#[
				"flute.air noise.closed.vowel varied",
				"bass.flute.multiphonic",
				"double bass.molto sul ponticello"],
			\dur, Pn(0.01,1)
		),
	// grain	
		~delays[15]+0.075,
		Pbind(
			\instrument, \grain,
			\group, ~fx,
			\out, ~master_mix_bus.subBus(21,1),
			\bufnum, ~sax_buf,
			\amp, 0.6,
			\dur, 8,		
			\atk, Pkey(\dur)*0.0001,
			\sus, Pkey(\dur)*0.9,
			\rel, Pkey(\dur)*0.5,
			\grainfreq, 3,
			\ratehigh, 1.5,
			\ratelow, 0.025,
			\graindur, 2,
			\center, 0
		),		
	// flute
		~delays[15]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \noteon,
			\voicename, \fl,
			\midinote, 69+Pseq([0,2],inf),
			\dur, Pseq([16,8,8,8],inf),
			\legato, 0.75,
			\amp, Pexprand(0.75,1.0,inf)
		),		
	// bass flute
		~delays[15]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \noteon,
			\voicename, \bfl,
			\midinote, 81+Pseq([0,2,3],inf), 
			\dur, Pseq([16,8,8,4],inf),
			\amp, Pexprand(0.1,0.25,inf)
		),		
	// contrabass
		~delays[15]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \noteon,
			\voicename, \cb,
			\midinote, 26,
			\dur, 64,
			\legato, 0.99,
			\amp, Pn(0.5,1)
		)
	], 1),
0);
)