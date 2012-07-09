/************************************	*/
/* Ensemble Piece		             	*/
/*	PATTERNS	8-12					*/
/************************************	*/

(
~durations[8] = 80; ~delays[8] = 0;
~durations[9] = 120; ~delays[9] = 0;
~durations[10] = 32; ~delays[10] = 0;
~durations[11] = 64; ~delays[11] = 4;
~durations[12] = 64; ~delays[12] = 0;

////////////////////////////////////////////////////////////////////////////////
~sections[8] = Pfindur(~durations[8],
	Ptpar([
	// program changes
		~delays[8],
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \program,
			\voicename, [\fl,\bfl,\bcl,\tr1,\tr2,\tb1,\tb2,\sx1,\vc],
			\programname, 
				#["flute.air noise.closed.vowel varied",
				"bass.flute.multiphonic",
				"bass clarinet boehm system.ordinario",
				"trumpet in c.ordinario",
				"trumpet in c.ordinario",
				"tenor trombone.ordinario",		
				"tenor trombone.ordinario",		
				"alto saxophone.slap.percussive slap",
				"violoncello.col legno battuto.ordinario"],
			\dur, Pn(0.01,1)
		),
	// flute
		~delays[8]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \noteon,
			\voicename, \fl,
			\midinote, 69+Pseq([0,2],inf),
			\dur, Prand([28,12],2),
			\legato, 0.75,
			\amp, Pexprand(0.75,1.0,inf)
		),			
	// bass flute
		~delays[8]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\rest,\noteon,\noteon],1),
			\voicename, \bfl,
			\midinote, Pseq([84,84,81],inf), 
			\dur, Prand([26,12,8],inf),
			\amp, Pexprand(0.1,0.25,inf)
		),
	// bfl -> reverb
		~delays[9]+0.05,
		Pbind(
			\instrument, \gverb,
			\group, ~fx,
			\in, ~master_dry_bus.subBus(1,1),
			\out, 24,
			\dur, ~durations[8],
			\atk, 0.01,
			\sus, ~durations[8],
			\rel, 0.01, 
			\amp, 1.5,
			\roomsize, 243, 
			\revtime, 15, 
			\damping, 0.3, 
			\inputbw, 0.34, 
			\drylevel, -80, 
			\earlylevel, -11, 
			\taillevel, -9
		),						
//		// bass clarinet
//			~delays[8]+0.05,
//			Pbind(
//				\type, \ctosc, 
//				\oscout, ~osc_destination,
//				\osccmd, Pseq([\rest,\noteon],1),
//				\voicename, \bcl,
//				\midinote, 81, 
//				\dur, Prand([24,12],inf),
//				\amp, Pexprand(0.1,0.25,inf)
//
//			),					
	// trumpet 1
		~delays[8]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,\rest],inf),
			\voicename, \tr1,
			\midinote, 
				Pseq((~hseries[3]-48).select({|n,i| 
					n>=24}).select({|n,i| n<=65}).reverse,inf), 
			\legato, 0.2,
			\dur, Pn(1/4,8),
			\amp, Pexprand(0.3,0.6,inf)
		),
	// trumpet 2
		~delays[8]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,\rest],inf),
			\voicename, \tr2,
			\midinote, 
				Pseq((~hseries[2]-48).select({|n,i| 
					n>=24}).select({|n,i| n<=65}).reverse,inf), 
			\legato, 0.2,
			\dur, Pn(1/4,32),
			\amp, Pexprand(0.3,0.6,inf)
		),
	// trumpet -> warp 
		~delays[8]+0.045,
		Pbind(
			\instrument, \warp,
			\group, ~fx,
			\in, ~master_dry_bus.subBus(4,1),
			\out, 17,
			\dur, 8,
			\atk, 8,
			\sus, 8 * 0.5,
			\rel, 8 * 0.99, 
			\amp, 0.6,
			\warpfactor, (-5,-3..5).midiratio,
			\freqscale, Pkey(\warpfactor)
		),									
	// trombone 1
		~delays[8]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\rest,\noteon],8),
			\voicename, \tb1,
			\midinote, 
				Pseq(~hseries[1].select({|n,i| 
					n>=30}).select({|n,i| n<=80}),inf), 
			\legato, 0.2,
			\dur, Pn(1/4,8),
			\amp, Pexprand(0.3,0.5,inf)
		),
	// trombone 2
		~delays[8]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\rest,\noteon],inf),
			\voicename, \tb2,
			\midinote, 
				Pseq(~hseries[0].select({|n,i| 
					n>=30}).select({|n,i| n<=80}),inf), 
			\legato, 0.2,
			\dur, Pn(1/4,32),
			\amp, Pexprand(0.3,0.5,inf)
		),
	// trombone -> warp 
		~delays[8]+0.045,
		Pbind(
			\instrument, \warp,
			\group, ~fx,
			\in, ~master_dry_bus.subBus(4,1),
			\out, 17,
			\dur, 16,
			\atk, 8,
			\sus, 16 * 0.5,
			\rel, 16 * 0.99, 
			\amp, 0.6,
			\warpfactor, (-5,-3..5).midiratio,
			\freqscale, Pkey(\warpfactor)
		),									
	// saxophone
		~delays[8]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\rest,Pn(\noteon,inf)],1),
			\voicename, \sx1,
			\midinote, Pseq([50,51],32), 
			\dur, Pseq([16,Pn(1/8,32)],1),
			\amp, Pseq([0,Pseg(Pseq([0.2,1,0.0],1),Pseq(1!4,1))],1)
		),
	// saxophone -> warp
		~delays[8]+16,
		Pbind(
			\instrument, \warp,
			\group, ~fx,
			\in, ~master_dry_bus.subBus(6,1),
			\out, 17,
			\dur, (~durations[8]-16),
			\atk, (~durations[8]-16) * 0.2,
			\sus, (~durations[8]-16) * 0.1,
			\rel, (~durations[8]-16) * 0.7, 
			\amp, 1.0,
			\warpfactor, [-12,-24,-36,-7,-5,-3].midiratio,
			\freqscale, Pkey(\warpfactor)
		),
	// violoncello
		~delays[8]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\rest,Pwrand([\noteon,\rest],[0.75,0.25],inf)],1),
			\voicename, \vc,
			\midinote, 
				Pseg(
					Pseq([
						Prand(~hseries[1].select({|n,i| n>=36}).select({|n,i| n<=73}),1),
						Prand(
							symmetricDifference(
								~hseries[0],
								~hseries[2]
							).select({|n,i| n>=36}).select({|n,i| n<=73}),
						inf)
					], inf),
					Pseq((~durations[8]/4)!4,inf),
					\lin,
					inf
				),
			\dur, 
				Pseq([48,Prand([Pn(1/8,8),Pn(1/6,6),
					Pn(1/3,3),1,2,4,Pn(1/4,4),Pn(1/5,5)],inf)],1),
			\amp, Pexprand(0.25,1.0,inf)
		),
	// violoncello -> pitchshift 
		~delays[8]+48,
		PmonoArtic(
			\pitchshift,
			\group, ~fx,
			\in, ~master_dry_bus.subBus(10,1),
			\out, ~master_fx_bus.subBus(10,1), // pitchshift -> hala
			\dur, (~durations[8]-48) / 381,
			\atk, (~durations[8]-48),
			\sus, (~durations[8]-48) * 0.1,
			\rel, (~durations[8]-48) * 0.5, 
			\amp, 4.0,
			\ratio, 2.0!3,
			\windowSize, 2.0,
			\timeDispersion, Pseq((0.00001,0.005..1.9),1),
			\pitchDispersion, Pseq((0.00001,0.005..1.9),1),
			\legato, 1.1
		),
	// pitchshift -> hala
		~delays[8]+0.05,
		PmonoArtic(
			\hala,
			\group, ~output,
			\in, ~master_fx_bus.subBus(10,1),
			\out, 26, 
			\dur, ~durations[8] / 381,
			\atk, ~durations[8] * 0.5,
			\sus, ~durations[8] * 0.99,
			\rel, ~durations[8] * 0.01, 
			\amp, 1.0,
			\pan, Pbrown(-1.0,1.0,0.15,inf),
			\legato, 1.1
		),
	// pitchshift -> warp 
		~delays[8]+0.05,
		Pbind(
			\instrument, \warp,
			\group, ~fx,
			\in, ~master_fx_bus.subBus(10,1),
			\out, 17,
			\dur, ~durations[8],
			\atk, ~durations[8] * 0.6,
			\sus, ~durations[8] * 0.1,
			\rel, ~durations[8] * 0.3, 
			\amp, 0.75,
			\warpfactor, [-12,-24,-36,-7,-5].midiratio,
			\freqscale, Pkey(\warpfactor)
		),
	// bass drum
		~delays[8]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\rest,Pn(\noteon,inf)],1),
			\voicename, \bd,
			\midinote, Prand([61,62],inf),
			\dur, Pseq([45,Pseq([Pn(1,12),2],inf)],1),
			\amp, 
				Pseq([0,Pseg(Pseq([0.01,1.0],inf),
					Pseq(~durations[8]-45!2,inf),\lin,inf)],1)
		)									
	], 1),
);

////////////////////////////////////////////////////////////////////////////////
~sections[9] = Pfindur(~durations[9],
	Ptpar([
	// program changes
		0,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \program,
			\voicename, [\fl,\bcl,\tb1,\tb2,
				\vi1,\vi2,\vi3,\vi4,\va1,\va2,\vc,\cb],
			\programname, 
				#["flute.ordinario",
				"bass clarinet boehm system.multiphonic.Vilhjalmsson Buffet",
				"tenor trombone.ordinario",
				"tenor trombone.ordinario",		
				"violin.ordinario",
				"violin.excessive pressure",
				"violin.ordinario",
				"violin.excessive pressure",	
				"viola.harmonic.artificial.fourth",
				"viola.harmonic.artificial.fourth",
				"violoncello.flautando",
				"double bass.molto sul ponticello"],
			\dur, Pn(0.01,1)
		),
	// bass clarinet
		~delays[9]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\rest,\noteon],1),
			\voicename, \bcl,
			\midinote, Prand([91,92],2), 
			\dur, Pseq([80,Prand([16,32],inf)],1),
			\amp, 1
		),					
	// trombone 1
		~delays[9]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\rest,Prand([\noteon,\rest],inf)],8),
			\voicename, \tb1,
			\midinote, 
				Pseq((~hseries[1]-48).select({|n,i| 
					n>=75}).select({|n,i| n<=83}),inf), 
			\dur, Pseq([80,Prand([8,4,16],inf)],1),
			\amp, Pexprand(0.3,0.5,inf)
		),		
	// trombone 2
		~delays[9]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\rest,Prand([\noteon,\rest],inf)],8),
			\voicename, \tb2,
			\midinote, 
				Pseq((~hseries[2]-48).select({|n,i| 
					n>=75}).select({|n,i| n<=83}),inf), 
			\dur, Pseq([80,Prand([8,4,16],inf)],1),
			\amp, Pexprand(0.3,0.5,inf)
		),							
	// violin 1
		~delays[9]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\rest,Pwrand([\rest,\noteon],[0.6,0.4],inf)],1),
			\voicename, \vi1,
			\midinote, 
				Prand(union(~hseries[0],~hseries[1]).select({|n,i| 
					n>=70}).select({|n,i| n<=104}),inf), 
			\dur, Pseq([32,Prand([4,8,16],inf)],inf),
			\amp, Pexprand(0.5,1.0,inf)
		),
	// violin 2
		~delays[9]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\rest,Prand([\rest,\noteon],inf)],1),
			\voicename, \vi2,
			\midinote, 
				Prand(union(~hseries[1],~hseries[3]-48).select({|n,i| 
					n>=55}).select({|n,i| n<=84}),inf), 
			\legato, 0.1,
			\dur, Pseq([32,Pwrand(1/[4,2,1],[0.5,0.2,0.1].normalizeSum,inf)],inf),
			\amp, Pexprand(0.5,1.0,inf)
		),		
	// violin 3
		~delays[9]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\rest,Pwrand([\rest,\noteon],[0.6,0.4],inf)],1),
			\voicename, \vi3,
			\midinote, 
				Prand(union(~hseries[0],~hseries[1]).select({|n,i| 
					n>=70}).select({|n,i| n<=104}),inf), 
			\dur, Pseq([16,Prand([4,8,16],inf)],inf),
			\amp, Pexprand(0.5,1.0,inf)
		),				
	// violin 4
		~delays[9]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\rest,Prand([\rest,\noteon],inf)],1),
			\voicename, \vi4,
			\midinote, 
				Prand(union(~hseries[1],~hseries[3]-48).select({|n,i| 
					n>=55}).select({|n,i| n<=84}),inf), 
			\legato, 0.1,
			\dur, Pseq([32,Pwrand(1/[4,2,1],[0.5,0.2,0.1].normalizeSum,inf)],inf),
			\amp, Pexprand(0.5,1.0,inf)
		),
	// viola 1
		~delays[9]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\rest,Pn(\noteon,inf)],1),
			\voicename, \va1,
			\midinote, 
				Prand(difference(~hseries[0],~hseries[3]-48).select({|n,i| 
					n>=73}).select({|n,i| n<=109}),inf), 
			\legato, 0.2,
			\dur, Pseq([8,Prand([4,2,8],inf)],inf),
			\amp, Pexprand(0.75,1.0,inf)
		),
	// viola 2
		~delays[9]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\rest,Pn(\noteon,inf)],1),
			\voicename, \va2,
			\midinote, 
				Prand(union(~hseries[0],~hseries[3]-48).select({|n,i| 
					n>=73}).select({|n,i| n<=109}),inf), 
			\legato, 0.2,
			\lag, Prand(1/[16,8,4,2,1],inf),
			\dur, Pseq([16,Prand([4,2,8],inf)],inf),
			\amp, Pexprand(0.75,1.0,inf)
		),
	// viola -> reverb
		~delays[9]+0.05,
		Pbind(
			\instrument, \gverb,
			\group, ~fx,
			\in, ~master_dry_bus.subBus(9,1),
			\out, 24,
			\dur, ~durations[9],
			\atk, ~durations[9]*0.1,
			\sus, ~durations[9],
			\rel, 10, 
			\amp, 1.0,
			\roomsize, 243, 
			\revtime, 15, 
			\damping, 0.3, 
			\inputbw, 0.34, 
			\drylevel, -80, 
			\earlylevel, -11, 
			\taillevel, -9
		),				
	// viola -> warp 
		~delays[9]+0.15,
		Pbind(
			\instrument, \warp,
			\group, ~fx,
			\addAction, 0, 
			\in, ~master_dry_bus.subBus(9,1),
			\out, ~master_fx_bus.subBus(9,1),
			\dur, ~durations[9],
			\atk, ~durations[9] * 0.4,
			\sus, ~durations[9] * 0.3,
			\rel, ~durations[9] * 0.3, 
			\amp, 0.75,
			\warpfactor, [12,-7,-12,-24,-36,-48].midiratio,
			\freqscale, Pkey(\warpfactor)
		),
	// warp -> fbdelay 
		~delays[9]+0.05,
		Pbind(
			\instrument, \fbdelay,
			\group, ~fx,
			\in, ~master_fx_bus.subBus(9,1),
			\out, [~master_fx_bus.subBus(23,1),~master_fx_bus.subBus(24,1)],
//			\out, ~master_fx_bus.subBus(23,1),
			\dur, ~durations[9],
			\atk, ~durations[9] * 0.4,
			\sus, ~durations[9] * 0.3,
			\rel, ~durations[9] * 0.3, 
			\amp, 1.0,
			\maxdelay, 11,
			\delay, (3,5..11),
			\fb, 0.25
		),	
	// fbdelay -> reverb
		~delays[9]+0.05,
		Pbind(
			\instrument, \gverb,
			\group, ~fx,
			\addAction, 1, // after fbdelay
			\in, ~master_fx_bus.subBus(24,1),
			\out, 24,
			\dur, ~durations[9],
			\atk, ~durations[9],
			\sus, 0.01,
			\rel, 10, 
			\amp, 1.0,
			\roomsize, 243, 
			\revtime, 15, 
			\damping, 0.3, 
			\inputbw, 0.34, 
			\drylevel, -80, 
			\earlylevel, -11, 
			\taillevel, -9
		),	
	// fbdelay -> hala 
		~delays[9]+0.25,
		PmonoArtic(
			\hala,
			\group, ~output,
			\in, ~master_fx_bus.subBus(23,1),
			\out, 26, 
			\dur, ~durations[9] / 120,
			\atk, ~durations[9] * 0.01,
			\sus, ~durations[9] * 0.99,
			\rel, ~durations[9] * 0.01, 
			\amp, 1.0,
			\pan, Pbrown(-1.0,1.0,0.15,inf),
			\legato, 1.1
		),
	// violoncello
		~delays[9]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\rest,Prand([\rest,\noteon],inf)],1),
			\voicename, \vc,
			\midinote, 											Prand(~hseries[0].select({|n,i| 
					n<=84}).select({|n,i| n>=36}),inf), 
			\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
			\amp, Pexprand(0.5,1.0,inf)
		),
	// double bass
		~delays[9]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \noteon,
			\voicename, \cb,
			\midinote, 26, 
			\dur, 120, //~durations[9]
			\amp, 1.0
		),
	// double bass -> freqshift 
		~delays[9]+0.05,
		PmonoArtic(
			\freqshift,
			\group, ~fx,
			\in, ~master_dry_bus.subBus(11,1),
			\out, 19,
			\dur, 100/Pseg(Prand(~hseries[0],inf),
				Prand((1..10),inf),\exp,inf),
			\atk, ~durations[9],
			\sus, 0.01,
			\rel, 0.01, 
			\amp, 1.5,
			\freq, 26.midicps*Pseg(Prand(1/(1,3..65)++(2,4..64),inf),
				Prand(10/(1..10),inf),\exp,inf),
			\legato, 2.0
		),
	// tam-tam
		~delays[9]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \noteon,
			\voicename, \tam,
			\midinote, 64, 
			\dur, Pn(32,1),
			\amp, Pexprand(0.7,1.0,1)		
		),
	// bass drum
		~delays[9]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pn(\noteon,inf),
			\voicename, \bd,
			\midinote, Prand([61,62],inf),
			\dur, Pseq([32,Pseq([Pn(1,12),2],inf)],1),
			\amp, 
				Pseq([1,Pseg(Pseq([0.01,1.0],inf),
					Pseq(~durations[9]-45!2,inf),\lin,inf)],1)
		)			
	], 1),
);
////////////////////////////////////////////////////////////////////////////////
~sections[10] = Pfindur(~durations[10],
	Ptpar([
	// program changes
		~delays[10],
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \program,
			\voicename, [\bfl,\tb1,\tb2,\vi1,\vi3,\cb],
			\programname, 
				#[
				"bass.flute.multiphonic",		
				"tenor trombone.ordinario",
				"tenor trombone.ordinario",
				"violin.excessive pressure",
				"violin.excessive pressure",
				"double bass.pizzicato.bartok"],
			\dur, Pn(0.01,1)
		),
	// bass flute
		~delays[10]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \noteon,
			\voicename, \bfl,
			\midinote, 81+Pseq([0,2,3],inf), 
			\dur, Pseq([16,8,8,4],inf),
			\amp, Pexprand(0.1,0.25,inf)
		),			
	// trombone 1
		~delays[10]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\noteon,\rest],inf)],1),
			\voicename, \tb1,
			\midinote, 
				Pseq((~hseries[1]-48).select({|n,i| 
					n>=75}).select({|n,i| n<=83}),inf), 
			\dur, Prand([8,4,16],3),
			\amp, Pexprand(0.3,0.5,inf)
		),		
	// trombone 2
		~delays[10]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\noteon,\rest],inf)],1),
			\voicename, \tb2,
			\midinote, 
				Pseq((~hseries[2]-48).select({|n,i| 
					n>=75}).select({|n,i| n<=83}),inf), 
			\dur, Prand([8,4,16],3),
			\amp, Pexprand(0.3,0.5,inf)
		),							
	// violin 1
		~delays[10]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Prand([\rest,\noteon],inf),
			\voicename, \vi1,
			\midinote, 
				Prand(union(~hseries[1],~hseries[3]-48).select({|n,i| 
					n>=55}).select({|n,i| n<=84}),inf), 
			\legato, 0.1,
			\dur, Pwrand(1/[4,2,1],[0.5,0.2,0.1].normalizeSum,inf),
			\amp, Pexprand(0.1,1.0,inf)
		),	
	// violin 3
		~delays[10]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Prand([\rest,\noteon],inf),
			\voicename, \vi3,
			\midinote, 
				Prand(union(~hseries[1],~hseries[3]-48).select({|n,i| 
					n>=55}).select({|n,i| n<=84}),inf), 
			\legato, 0.1,
			\dur, Pwrand(1/[4,2,1],[0.5,0.2,0.1].normalizeSum,inf),
			\amp, Pexprand(0.1,1.0,inf)
		),
	// double bass
		~delays[10]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \noteon,
			\voicename, \cb,
			\midinote, 26, 
			\dur, Pn(32,1),
			\amp, 1.0
		)							
	], 1),
);
////////////////////////////////////////////////////////////////////////////////
~sections[11] = Pfindur(~durations[11],
	Ptpar([
	// program changes
		~delays[11],
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \program,
			\voicename, [\vi1,\vi2,\vi3,\vi4,\va1,\va2,\vc,\cb],
			\programname, 
				#["violin.excessive pressure",
				"violin.ordinario",
				"violin.excessive pressure",
				"violin.ordinario",
				"viola.excessive pressure",	
				"viola.ordinario",	
				"violoncello.excessive pressure",
				"double bass.ordinario"],
			\dur, Pn(0.01,1)
		),
	// violin 1
		~delays[11]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, 
				Pwrand([\noteon,\rest], 
					Pseq([Array.fib(8,1,1).reciprocal,
						Array.fib(8,1,1).reverse.reciprocal
						].flop,8).collect(_.normalizeSum),8),
			\voicename, \vi1,
			\midinote, 
				Prand(union(~hseries[1],~hseries[3]-48).select({|n,i| 
					n>=55}).select({|n,i| n<=84}),inf), 
			\dur, Prand((2,4..8),inf),	
			\legato, 0.99,
			\amp, Pexprand(0.75,1.0,inf)
		),	
	// violin 2
		~delays[11]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, 
				Pwrand([\rest,\noteon], 
					Pseq([Array.fib(8,1,1).reciprocal,
						Array.fib(8,1,1).reverse.reciprocal
						].flop,8).collect(_.normalizeSum),inf),
			\voicename, \vi2,
			\midinote, 
				Prand(union(~hseries[0],~hseries[2]-48).select({|n,i| 
					n>=55}).select({|n,i| n<=84}),inf), 
			\dur, Prand((2,4..8),inf),	
			\legato, 0.99,
			\amp, Pexprand(0.75,1.0,inf)
		),	
	// violin 3
		~delays[11]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, 
				Pwrand([\noteon,\rest], 
					Pseq([Array.fib(8,1,1).reciprocal,
						Array.fib(8,1,1).reverse.reciprocal
						].flop,8).collect(_.normalizeSum),8),
			\voicename, \vi3,
			\midinote, 
				Prand(union(~hseries[1],~hseries[3]-48).select({|n,i| 
					n>=55}).select({|n,i| n<=84}),inf), 
			\dur, Prand((2,4..8),inf),	
			\legato, 0.99,
			\amp, Pexprand(0.75,1.0,inf)
		),	
	// violin 4
		~delays[11]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, 
				Pwrand([\rest,\noteon], 
					Pseq([Array.fib(8,1,1).reciprocal,
						Array.fib(8,1,1).reverse.reciprocal
						].flop,8).collect(_.normalizeSum),inf),
			\voicename, \vi4,
			\midinote, 
				Prand(union(~hseries[0],~hseries[2]-48).select({|n,i| 
					n>=55}).select({|n,i| n<=84}),inf), 
			\dur, Prand((2,4..8),inf),	
			\legato, 0.99,
			\amp, Pexprand(0.75,1.0,inf)
		),		
	// viola 1
		~delays[11]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, 
				Pwrand([\noteon,\rest], 
					Pseq([Array.fib(8,1,1).reciprocal,
						Array.fib(8,1,1).reverse.reciprocal
						].flop,8).collect(_.normalizeSum),8),
			\voicename, \va1,
			\midinote, 
				Prand(union(~hseries[2]-12,~hseries[4]-48).select({|n,i| 
					n>=48}).select({|n,i| n<=94}),inf), 
			\dur, Prand((2,4..8),inf),	
			\legato, 0.99,
			\amp, Pexprand(0.75,1.0,inf)
		),	
	// viola 1
		~delays[11]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, 
				Pwrand([\rest,\noteon], 
					Pseq([Array.fib(8,1,1).reciprocal,
						Array.fib(8,1,1).reverse.reciprocal
						].flop,8).collect(_.normalizeSum),inf),
			\voicename, \va1,
			\midinote, 
				Prand(union(~hseries[1]-12,~hseries[3]-48).select({|n,i| 
					n>=48}).select({|n,i| n<=94}),inf), 
			\dur, Prand((2,4..8),inf),
			\legato, 0.99,	
			\amp, Pexprand(0.75,1.0,inf)
		),	
	// violoncello
		~delays[11]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, 
				Pwrand([\noteon,\rest], 
					Pseq([Array.fib(8,1,1).reciprocal,
						Array.fib(8,1,1).reverse.reciprocal
						].flop,8).collect(_.normalizeSum),8),

			\voicename, \vc,
			\midinote,
				Prand(union(~hseries[0]-12,~hseries[3]-48).select({|n,i| 
					n>=36}).select({|n,i| n<=60}),inf), 
			\dur, Prand((2,4..8),inf),	
			\legato, 0.99,
			\amp, Pexprand(0.75,1.0,inf)
		),
	// contrabass
		~delays[11]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, 
				Pwrand([\rest,\noteon], 
					Pseq([Array.fib(8,1,1).reciprocal,
						Array.fib(8,1,1).reverse.reciprocal
						].flop,8).collect(_.normalizeSum),inf),

			\voicename, \cb,
			\midinote,
				Prand(union(~hseries[0]-12,~hseries[3]-48).select({|n,i| 
					n>=26}).select({|n,i| n<=60}),inf), 
			\dur, Prand((2,4..8),inf),	
			\legato, 0.99,
			\amp, Pexprand(0.75,1.0,inf)
		),			
	// crotales			
		~delays[11]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \noteon,
			\voicename, \ctl,
			\midinote, 
			Prand([
				Prand(~hseries[0].select({|n,i| 
					n>=72}).select({|n,i| n<=96}),Prand((2,4..32),1)),
				Ptuple([		// there must be a better way to write this...
					Prand(~hseries[0].select({|n,i| 
						n>=72}).select({|n,i| n<=96}),Prand((2,4..32),1)),
					Prand(~hseries[0].select({|n,i| 
						n>=72}).select({|n,i| n<=96}),Prand((2,4..32),1)),
					Prand(~hseries[0].select({|n,i| 
						n>=72}).select({|n,i| n<=96}),Prand((2,4..32),1)),
					Prand(~hseries[0].select({|n,i| 
						n>=72}).select({|n,i| n<=96}),Prand((2,4..32),1)),
				],1)], inf), 
			\dur, Prand([4,8,2,1,1/2],inf).collect({|dur| ~crotales_dur = dur; dur}),
			\legato, 2,
			\amp, Pexprand(0.7,1.0,inf)
		),
	// crotales -> bitcrush
		~delays[11]+0.075,
		Pbind(
			\instrument, \crusher,
			\group, ~fx,
			\in, ~master_dry_bus.subBus(12,1),
			\out, ~master_fx_bus.subBus(12,1),
			\dur, Pfunc({~crotales_dur}),
			\atk, Pkey(\dur) * 0.1,
			\sus, Pkey(\dur) * 0.1,
			\rel, Pkey(\dur) * 1.5, 
			\amp, 0.05,
			\lag, Pfunc({~crotales_dur.rand}),
			\bitdepth, Prand((1..4),inf)
		),	
	// bitcrush -> reverb
		~delays[11]+0.05,
		Pbind(
			\instrument, \gverb,
			\group, ~fx,
			\in, ~master_fx_bus.subBus(12,1),
			\out, 24,
			\dur, ~durations[11],
			\atk, 0.01,
			\sus, ~durations[11],
			\rel, 0.01, 
			\amp, 1.0,
			\roomsize, 243, 
			\revtime, 15, 
			\damping, 0.3, 
			\inputbw, 0.34, 
			\drylevel, -80, 
			\earlylevel, -11, 
			\taillevel, -9
		),								
	// crotales -> pitchshift
		~delays[11]+0.075,
		Pbind(
			\instrument, \pitchshift,
			\group, ~fx,
			\in, ~master_dry_bus.subBus(12,1),
			\out, 20,
			\dur, ~durations[11],
			\atk, ~durations[11],
			\sus, ~durations[11] * 0.01,
			\rel, ~durations[11] * 0.5,
			\amp, 0.75,
			\ratio, [0.25,0.5,1.5,2,2.5],
			\windowSize, 4.0,
			\pitchDispersion, {4.0.rand}.dup(4),
			\timeDispersion, {4.0.rand}.dup(4)
		),					
	// tam-tam
		~delays[11]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \noteon,
			\voicename, \tam,
			\midinote, Prand([65,66],1), 
			\dur, Prand([4,8,2,1,1/2],inf),
			\amp, Pexprand(0.7,1.0,inf)		
		),
	// bass drum
		~delays[11]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \noteon,
			\voicename, \bd,
			\midinote, Prand([61,62],inf),
			\dur, 32,
			\amp, Pexprand(0.7,1.0,1)		
		)									
	], 1),
);
////////////////////////////////////////////////////////////////////////////////
~sections[12] = Pfindur(~durations[12],
	Ptpar([
	// program changes
		0, // give some delay because of the massive amount of prog changes
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, \program,
			\voicename, [\fl,\bfl,\bcl,\bsn1,\bsn2,\sx1,\sx2,
						\vi1,\vi2,\vi3,\vi4,\va1,\va2,\vc,\cb],
			\programname, 
				#["flute.ordinario",
				"bass.flute.ordinario",
				"bass clarinet boehm system.ordinario",
				"bassoon.ordinario.Schawrz Heckel",
				"bassoon.ordinario.Schawrz Heckel",
				"alto saxophone.ordinario",
				"alto saxophone.ordinario",
				"violin.excessive pressure",
				"violin.ordinario",
				"violin.excessive pressure",	
				"violin.ordinario",	
				"viola.ordinario",
				"viola.ordinario",
				"violoncello.ordinario",
				"double bass.ordinario"],
			\dur, Pn(0.01,1)
		),
	// flute
		~delays[12]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \fl,
			\midinote, 
				Pseq([	
					Prand(~hseries[0].select({|n,i| 
						n>=59}).select({|n,i| n<=96}),1), 
					Prand(~hseries[1].select({|n,i| 
						n>=59}).select({|n,i| n<=96}),1), 
					Prand(~hseries[2].select({|n,i| 
						n>=59}).select({|n,i| n<=96}),1), 
					Prand(~hseries[3].select({|n,i| 
						n>=59}).select({|n,i| n<=96}),1), 
					Prand(~hseries[4].select({|n,i| 
						n>=59}).select({|n,i| n<=96}),1), 
				],1),
			\dur, 16,
			\legato, 0.99,
			\amp, Pexprand(0.1,0.3,inf)
		),
	// bass flute
		~delays[12]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \bfl,
			\midinote, 
				Pseq([	
					Prand(~hseries[0].select({|n,i| 
						n>=48}).select({|n,i| n<=84}),1), 
					Prand(~hseries[1].select({|n,i| 
						n>=48}).select({|n,i| n<=84}),1), 
					Prand(~hseries[2].select({|n,i| 
						n>=48}).select({|n,i| n<=84}),1), 
					Prand(~hseries[3].select({|n,i| 
						n>=48}).select({|n,i| n<=84}),1), 
					Prand(~hseries[4].select({|n,i| 
						n>=48}).select({|n,i| n<=84}),1), 
				],1),
			\dur, 16,
			\amp, 1.0
		),
	// bass clarinet
		~delays[12]+0.025,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \bcl,
			\midinote, 
				Pseq([	
					Prand(~hseries[0].select({|n,i| 
						n>=34}).select({|n,i| n<=85}),1), 
					Prand(~hseries[1].select({|n,i| 
						n>=34}).select({|n,i| n<=85}),1), 
					Prand(~hseries[2].select({|n,i| 
						n>=34}).select({|n,i| n<=85}),1), 
					Prand(~hseries[3].select({|n,i| 
						n>=34}).select({|n,i| n<=85}),1), 
					Prand(~hseries[4].select({|n,i| 
						n>=34}).select({|n,i| n<=85}),1), 
				],1),
			\dur, 16,
			\legato, 0.99,		
			\amp, Pexprand(0.1,0.3,inf)
		),
	// bassoon 1
		~delays[12]+0.025,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \bsn1,
			\midinote, 
				Pseq([	
					Prand(~hseries[0].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),1), 
					Prand(~hseries[1].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),1), 
					Prand(~hseries[2].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),1), 
					Prand(~hseries[3].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),1), 
					Prand(~hseries[4].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),1), 
				],1),
			\dur, 16,
			\legato, 0.99,					
			\amp, Pexprand(0.1,0.3,inf)
		),
	// bassoon 2
		~delays[12]+0.025,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \bsn2,
			\midinote, 
				Pseq([	
					Prand(~hseries[0].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),1), 
					Prand(~hseries[1].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),1), 
					Prand(~hseries[2].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),1), 
					Prand(~hseries[3].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),1), 
					Prand(~hseries[4].select({|n,i| 
						n>=34}).select({|n,i| n<=76}),1), 
				],1),
			\dur, 16,
			\legato, 0.99,					
			\amp, Pexprand(0.1,0.3,inf)
		),	
	// saxophone 1
		~delays[12]+0.025,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \sx1,
			\midinote, 
				Pseq([	
					Prand(~hseries[0].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),1), 
					Prand(~hseries[1].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),1), 
					Prand(~hseries[2].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),1), 
					Prand(~hseries[3].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),1), 
					Prand(~hseries[4].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),1), 
				],1),
			\dur, 16,
			\legato, 0.99,					
			\amp, Pexprand(0.1,0.3,inf)
		),
	// saxophone 2
		~delays[12]+0.025,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \sx2,
			\midinote, 
				Pseq([	
					Prand(~hseries[0].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),1), 
					Prand(~hseries[1].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),1), 
					Prand(~hseries[2].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),1), 
					Prand(~hseries[3].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),1), 
					Prand(~hseries[4].select({|n,i| 
						n>=49}).select({|n,i| n<=89}),1), 
				],1),
			\dur, 16,
			\legato, 0.99,					
			\amp, Pexprand(0.1,0.3,inf)
		),
	// violin 1
		~delays[12]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \vi1,
			\midinote, 
				Pseq([	
					Prand(~hseries[0].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Prand(~hseries[1].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Prand(~hseries[2].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Prand(~hseries[3].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Prand(~hseries[4].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
				],1),
			\dur, 16,
			\legato, 0.99,					
			\amp, Pexprand(0.4,0.7,1)
		),
	// violin 2
		~delays[12]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \vi2,
			\midinote, 
				Pseq([	
					Prand(~hseries[0].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Prand(~hseries[1].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Prand(~hseries[2].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Prand(~hseries[3].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Prand(~hseries[4].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
				],1),
			\dur, 16,
			\legato, 0.99,					
			\amp, Pexprand(0.4,0.7,inf)
		),
	// violin 3
		~delays[12]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \vi3,
			\midinote, 
				Pseq([	
					Prand(~hseries[0].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Prand(~hseries[1].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Prand(~hseries[2].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Prand(~hseries[3].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Prand(~hseries[4].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
				],1),
			\dur, 16,
			\legato, 0.99,					
			\amp, Pexprand(0.4,0.7,1)
		),
	// violin 4
		~delays[12]+0.05,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \vi4,
			\midinote, 
				Pseq([	
					Prand(~hseries[0].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Prand(~hseries[1].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Prand(~hseries[2].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Prand(~hseries[3].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
					Prand(~hseries[4].select({|n,i| 
						n>=70}).select({|n,i| n<=104}),1), 
				],1),
			\dur, 16,
			\legato, 0.99,					
			\amp, Pexprand(0.4,0.7,inf)
		),		
	// viola 1
		~delays[12]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \va1,
			\midinote, 
				Pseq([	
					Prand(~hseries[0].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),1), 
					Prand(~hseries[1].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),1), 
					Prand(~hseries[2].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),1), 
					Prand(~hseries[3].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),1), 
					Prand(~hseries[4].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),1), 
				],1),
			\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
			\legato, 0.99,					
			\amp, Pexprand(0.4,0.7,inf)
		),
	// viola 2
		~delays[12]+0.075,
		Pbind(
			\type, \ctosc, 
			\oscout, ~osc_destination,
			\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
			\voicename, \va2,
			\midinote, 	
				Pseq([	
					Prand(~hseries[0].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),1), 
					Prand(~hseries[1].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),1), 
					Prand(~hseries[2].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),1), 
					Prand(~hseries[3].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),1), 
					Prand(~hseries[4].select({|n,i| 
						n>=48}).select({|n,i| n<=94}),1), 
				],1),
			\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
			\legato, 0.99,					
			\amp, Pexprand(0.4,0.7,inf)
		),
	// violoncello
		~delays[12]+0.075,
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
			\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
			\legato, 0.99,					
			\amp, Pexprand(0.4,0.7,inf)
		),
	// contrabass
		~delays[12]+0.075,
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
			\dur, Pn(16,inf),
			\legato, 0.99,					
			\amp, Pexprand(0.1,0.3,inf)
		)	
	], 1),
0);
)