/************************************	*/
/* Ensemble Piece		             	*/
/*	PATTERNS	13-					*/
/************************************	*/


(
~durations[13] = 128; ~delays[13] = 0;

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
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \fl,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=59}).select({|n,i| n<=96}),1), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=59}).select({|n,i| n<=96}),1), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=59}).select({|n,i| n<=96}),1), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=59}).select({|n,i| n<=96}),1), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=59}).select({|n,i| n<=96}),1), 
				],1),
			\dur, Pseq([16,Pxrand([4,8,16],inf)],1).collect({|dur| ~wwdur = dur; dur}),
			\legato, 0.99,
			\amp, Pexprand(0.1,1.0,inf)
		),
	// bass flute
		~delays[13]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \bfl,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=48}).select({|n,i| n<=84}),1), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=48}).select({|n,i| n<=84}),1), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=48}).select({|n,i| n<=84}),1), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=48}).select({|n,i| n<=84}),1), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=48}).select({|n,i| n<=84}),1), 
				],1),
			\dur, Pfunc({~wwdur}),
			\amp, Pexprand(0.1,1.0,inf)
		),
	// bass clarinet
		~delays[13]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \bcl,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=34}).select({|n,i| n<=85}),1), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=34}).select({|n,i| n<=85}),1), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=34}).select({|n,i| n<=85}),1), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=34}).select({|n,i| n<=85}),1), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=34}).select({|n,i| n<=85}),1), 
				],1),
			\dur, Pfunc({~wwdur}),
			\legato, 0.99,		
			\amp, Pexprand(0.1,1.0,inf)
		),
	// bassoon 1
		~delays[13]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \bsn1,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),1), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),1), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),1), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),1), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),1), 
				],1),
			\dur, Pfunc({~wwdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.1,1.0,inf)
		),
	// bassoon 2
		~delays[13]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \bsn2,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),1), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),1), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),1), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),1), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),1), 
				],1),
			\dur, Pfunc({~wwdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.1,1.0,inf)
		),	
	// trumpet 1
		~delays[13]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \tr1,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=24}).select({|n,i| n<=84}),inf), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=24}).select({|n,i| n<=84}),inf), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=24}).select({|n,i| n<=84}),inf), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=24}).select({|n,i| n<=84}),inf), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=24}).select({|n,i| n<=84}),inf), 
				],1),
			\dur, Pseq([16,Prand([4,8,16],inf)],1).collect({|dur| ~brassdur = dur; dur}),
			\legato, 0.99,					
			\amp, Pexprand(0.1,0.75,inf)
		),
	// trumpet 2
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \tr2,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=24}).select({|n,i| n<=84}),inf), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=24}).select({|n,i| n<=84}),inf), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=24}).select({|n,i| n<=84}),inf), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=24}).select({|n,i| n<=84}),inf), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=24}).select({|n,i| n<=84}),inf), 
				],1),
			\dur, Pfunc({~brassdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.1,0.75,inf)
		),		
	// trombone 1
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \tb1,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=30}).select({|n,i| n<=80}),inf), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=30}).select({|n,i| n<=80}),inf), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=30}).select({|n,i| n<=80}),inf), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=30}).select({|n,i| n<=80}),inf), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=30}).select({|n,i| n<=80}),inf), 
				],1),
			\dur, Pfunc({~brassdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.1,0.75,inf)
		),
	// trombone 2
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \tb2,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=30}).select({|n,i| n<=80}),inf), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=30}).select({|n,i| n<=80}),inf), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=30}).select({|n,i| n<=80}),inf), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=30}).select({|n,i| n<=80}),inf), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=30}).select({|n,i| n<=80}),inf), 
				],1),
			\dur, Pfunc({~brassdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.1,0.75,inf)
		),		
	// saxophone 1
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \sx1,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),1), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),1), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),1), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),1), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),1), 
				],1),
			\dur, Pfunc({~brassdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.1,0.75,inf)
		),
	// saxophone 2
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \sx2,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),1), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),1), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),1), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),1), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),1), 
				],1),
			\dur, Pfunc({~brassdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.1,0.75,inf)
		),
	// violin 1
		~delays[13]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \vi1,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
				],1),
			\dur, Pseq([16,Prand([8,4,16],inf)]).collect({|dur| ~stringsdur = dur; dur}),
			\legato, 0.99,					
			\amp, Pexprand(0.4,1.0,inf)
		),
	// violin 2
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \vi2,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
				],1),
			\dur, Pfunc({~stringsdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.4,1.0,inf)
		),
	// violin 3
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \vi3,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
				],1),
			\dur, Pfunc({~stringsdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.4,1.0,inf)
		),
	// violin 4
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \vi4,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
				],1),
			\dur, Pfunc({~stringsdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.4,1.0,inf)
		),		
	// viola 1
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \va1,
			\midinote, 
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),1), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),1), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),1), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),1), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),1), 
				],1),
			\dur, Pfunc({~stringsdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.4,1.0,inf)
		),
	// viola 2
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \va2,
			\midinote, 	
				Pseq([	
					Pxrand(~hseries[0].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),1), 
					Pxrand(~hseries[1].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),1), 
					Pxrand(~hseries[2].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),1), 
					Pxrand(~hseries[3].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),1), 
					Pxrand(~hseries[4].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),1), 
				],1),
			\dur, Pfunc({~stringsdur}),
			\legato, 0.99,					
			\amp, Pexprand(0.4,1.0,inf)
		),
	// violoncello
		~delays[13]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \vc,
			\midinote, Pseq([
				~hseries[0][1],
				~hseries[1][2],
				~hseries[2][3],
				~hseries[3][4],
				~hseries[4][5]],1), 
			\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1).collect({|dur| ~bass = dur; dur}),
			\legato, 0.99,					
			\amp, Pexprand(0.4,1.0,inf)
		),
	// contrabass
		~delays[13]+0.08,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \noteon,
			\voicename, \cb,
			\midinote, Pseq([
				~hseries[0][0],
				~hseries[1][0],
				~hseries[2][0],
				~hseries[3][0],
				~hseries[4][0]],1),
			\dur, Pfunc({~bass}),
			\legato, 0.99,					
			\amp, Pexprand(0.4,1.0,inf)
		),
	// crotales			
		~delays[13]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \noteon,
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
					Prand([-2,-1,1,2]),
					Pseq([-1,1]),
					0),
			\dur, Prand(1/[4,Pn(1/8,8),Pn(1/6,6)],inf),
			\legato, 2,
			\amp, Pexprand(0.7,1.0,inf)
		),		
	// tam-tam
		~delays[13]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \noteon,
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
			\osccmd, Pn(\noteon,inf),
			\voicename, \bd,
			\midinote, Prand([61,62],inf),
			\dur, 32,
			\amp, Pexprand(0.7,1.0,1)		
		)				
	], 1),
0);
)