/************************************	*/
/* Ensemble Piece		             	*/
/*	PATTERNS						*/
/************************************	*/



(
~load_patterns = nil;

~scales = 0!32;
~sections = 0!32;
~durations = 8!32;
~delays = 0!32;

~durations[0] = 60; ~delays[0] = 0;
~durations[1] = 60; ~delays[1] = 1;
~durations[2] = 32; ~delays[2] = 8;
~durations[3] = 60; ~delays[3] = 8;
~durations[4] = 64; ~delays[4] = 0;
~durations[5] = 64; ~delays[5] = 0;

~pelog = Array.fill(10, {|i| Scale.pelog.degrees + (12*i) }).lace.sort;
	
~load_patterns = {
	////////////////////////////////////////////////////////////////////////////////
	~sections[0] = Pfindur(~durations[0],
		Ptpar([
			~delays[0],
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
			~delays[0]+0.05,
			Pdef(\section0_fl, 
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
			),
			~delays[0]+0.05,
			Pdef(\section0_warp,
				Pbind(
					\instrument, \warp,
					\group, ~fx,
					\in, ~master_fx_bus.subBus(0,1),
					\out, 17,
					\dur, ~durations[0],
					\atk, ~durations[0] * 0.1,
					\sus, ~durations[0] * 0.3,
					\rel, ~durations[0] * 0.8, // slight overlap with s2
					\amp, 0.45,
					\warpfactor, (3,5..11).midiratio,
					\freqscale, Pkey(\warpfactor)
				)
			),
			~delays[0]+0.05,
			Pdef(\section0_bfl,  
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, \bfl,
					\midinote, 81+Pseq([0,2,3],inf), 
					\dur, Pseq([16,8,8,4],inf),
					\amp, Pexprand(0.1,0.25,inf)
				)	 
			),
			~delays[0]+0.05,
			Pdef(\section0_bcl, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\rest,Pwrand([\rest,\noteon],[0.25,0.75],inf)],1),
					\voicename, \bcl,
					\midinote, 81, 
					\dur, Pseq([8,Prand([4,8,16],inf)],inf),
					\amp, Pexprand(0.1,0.25,inf)
				)	 
			),
			~delays[0]+0.05,
			Pdef(\section0_va1, 
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
		], 1)
	);
	
	////////////////////////////////////////////////////////////////////////////////
	~sections[1] = Pfindur(~durations[1],
		Ptpar([
			~delays[1],
			Pdef(\section1_pg, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \program,
					\voicename, [\fl,\bfl,\bcl,\sx1,\vi1,\vi2,\va1,\va2],
					\programname, 
						#["flute.air noise.closed.vowel varied",
						"bass.flute.jet whistle",
						"bass clarinet boehm system.multiphonic.Vilhjalmsson Buffet",
						"alto saxophone.slap.percussive slap",
						"violin.harmonic.artificial.fourth",
						"violin.harmonic.artificial.fourth",
						"viola.harmonic.artificial.fourth",
						"viola.harmonic.artificial.fourth"],
					\dur, Pn(0.01,1)
				)	
				 
			),
			~delays[1]+0.05,
			Pdef(\section1_fl, 
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
			),
			~delays[1]+0.05,
			Pdef(\section1_bfl,  
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\noteon,\rest],inf)],1),
					\voicename, \bfl,
					\midinote, Prand([49,52,53,56,59,62],inf), 
					\dur, Prand([16,8,32],inf),
					\amp, Pexprand(0.1,1.0,inf)
				)	
			),
			~delays[1]+0.05,
			Pdef(\section1_bcl,  
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\rest,\noteon,Pwrand([\rest,\noteon],[0.5,0.5],inf)],1),
					\voicename, \bcl,
					\midinote, Pseq([121,122],inf), 
					\dur, Pseq([8,Prand([4,8,16],inf)],inf),
					\amp, Pexprand(0.1,0.25,inf)
				)	
			),
			~delays[1]+0.05,
			Pdef(\section1_sx1, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\rest,Pn(\noteon,inf)],1),
					\voicename, \sx1,
					\midinote, Pseq([50,51],32), 
					\dur, Pseq([8,Pn(1/16,32)],inf),
					\amp, Pseq([0,Pseg(Pseq([0.2,1,0.0],1),Pseq(1!2,1))],1)
				)				
			),
			~delays[1]+0.05,
			Pdef(\section1_tam, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, \tam,
					\midinote, 67, 
					\dur, Pn(16,1),
					\amp, Pexprand(0.7,1.0,1)
				)				
			),
			~delays[1]+0.05,
			Pdef(\section1_recbuf,
				Pbind(
					\instrument, \recbuf,
					\group, ~fx,
					\in, ~master_fx_bus.subBus(13,1),
					\dur, Pn(16,1),
					\bufnum, ~tamtam_buf
				)
			),
			~delays[1]+0.05,
			Pdef(\section1_vi1,  
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Prand([\rest,\noteon],inf),
					\voicename, \vi1,
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=81}),inf), 							\dur, Pseq([Pn(6,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.1,0.6,inf)
				)	 
			),
			~delays[1]+0.05,
			Pdef(\section1_vi2,  
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Prand([\rest,\noteon],inf),
					\voicename, \vi2,
					\midinote, 											Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=81}),inf), 
					\dur, Pseq([Pn(6,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.1,0.6,inf)
				)	 
			),
			~delays[1]+0.05,
			Pdef(\section1_va1,  
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
			),
			~delays[1]+0.05,
			Pdef(\section1_va2,  
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
		], 1),
	0);
	
	////////////////////////////////////////////////////////////////////////////////
	~sections[2] = Pfindur(~durations[2],
		Ptpar([
			~delays[2],
			Pdef(\section2_pg, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \program,
					\voicename, [\fl,\bfl,\bcl,\bsn1,\sx1,\sx2,\cb,\vc],
					\programname, 
						#["flute.ordinario",
						"bass.flute.ordinario",
						"bass clarinet boehm system.ordinario",
						"bassoon.ordinario.Schwarz Heckel",
						"alto saxophone.slap.percussive slap",
						"alto saxophone.multiphonic.Gubler Selmer_Super_Action_II",
						"double bass.pizzicato.bartok",
						"violoncello.ordinario"],
					\dur, Pn(0.01,1)
				)		 
			),
			~delays[2]+0.05,
			Pdef(\section2_freeze, 
				Pbind(
					\instrument, \freeze,
					\group, ~fx,
					\out, 18,
					\bufnum, ~tamtam_buf,
					\bufdur, ~tamtam_buf.duration,
					\dur, Pseq([16,16],1),
					\cfreq, Pkey(\dur).reciprocal*0.5, 
					\cphase, 0,
					\cmul, Pkey(\bufdur),
					\cadd, 0,
					\atk, Pkey(\dur)*0.2,
					\sus, Pkey(\dur)*0.4,
					\rel, Pkey(\dur)*0.4, 
					\amp, Pseq([0.0,0.5],1)
				)				 
			),
			~delays[2]+0.05,
			Pdef(\section2_fl, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \fl,
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=59}).select({|n,i| n<=96}),inf), 					\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.4,1.0,inf)
				)	
			),
			~delays[2]+0.05,
			Pdef(\section2_bfl, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \bfl,
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=48}).select({|n,i| n<=84}),inf), 					\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.7,1.0,inf)
				)	
			),
			~delays[2]+0.05,
			Pdef(\section2_bcl, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, \bcl,
					\midinote, Pseq([81,83],1), 
					\dur, Pn(16,2),
					\amp, Pexprand(0.1,0.5,inf)
				)	 
			),
			~delays[2]+0.05,
			Pdef(\section2_bsn1, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \bsn1,
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=48}).select({|n,i| n<=76}),inf), 					\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.7,1.0,inf)
				)	
			),
			~delays[2]+0.05,
			Pdef(\section2_sx1, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, \sx1,
					\midinote, Pseq([50,51],32), 
					\dur, Pseq([8,Pn(1/8,32)],inf),
					\amp, Pseq([0,Pseg(Pseq([0.2,1,0.0],1),Pseq(2!2,1))],1)
				)	
			),
			~delays[2]+0.05,
			Pdef(\section2_sx2, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\rest,\noteon],1),
					\voicename, \sx2,
					\midinote, Pseq([0,107],1), 
					\dur, Pseq([16,16],1),
					\amp, Pseq([0,1],1)
				)	
			),
			~delays[2]+0.05,
			Pdef(\section2_sxpitchshift, 
				Pbind(
					\instrument, \pitchshift, 
					\group, ~fx,
					\in, ~master_fx_bus.subBus(6,1),
					\out, 20,
					\dur, Pseq([16,Pn(16,2)],inf),
					\atk, Pkey(\dur)*0.5,
					\sus, Pkey(\dur)*0.3,
					\rel, Pkey(\dur)*0.3, 
					\amp, Pseq([0,1.0],1),
					\ratio, [0.5+0.001.rand,0.5-0.001.rand], // fatten it out
					\timeDispersion, 0.0001.rand
				)	
			),
			~delays[2]+0.05,
			Pdef(\section2_cb, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, \cb,
					\midinote, Pwrand([38,26],[0.7,0.3],inf), 
					\dur, Pn(16,2),
					\amp, Pexprand(0.85,1.0,inf)
				)	
			),
			~delays[2]+0.05,
			Pdef(\section2_vi1, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \vi1,
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=81}),inf), 							\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.9,1.0,inf)
				)	
			),
			~delays[2]+0.05,
			Pdef(\section2_vi2, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \vi2,
					\midinote, 											Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=81}),inf), 
					\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.9,1.0,inf)
				)	
			),
			~delays[2]+0.05,
			Pdef(\section2_vi3, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \vi3,
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=81}),inf), 							\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.9,1.0,inf)
				)	
			),
			~delays[2]+0.05,
			Pdef(\section2_vi4, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \vi4,
					\midinote, 											Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=81}),inf), 
					\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.9,1.0,inf)
				)	
			),
			~delays[2]+0.05,
			Pdef(\section2_va1, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \va1,
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=73}),inf), 							\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.9,1.0,inf)
				)	
			),
			~delays[2]+0.05,
			Pdef(\section2_va2, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \va2,
					\midinote, 											Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=73}),inf), 
					\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.9,1.0,inf)
				)	
			),
			~delays[2]+0.05,
			Pdef(\section2_vc, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \vc,
					\midinote, 											Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n<=84}).select({|n,i| n>=36}),inf), 
					\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.9,1.0,inf)
				)	
			)
		], 1),
	0);
	
	////////////////////////////////////////////////////////////////////////////////
	~sections[3] = Pfindur(~durations[3],
		Ptpar([
			~delays[3],
			Pdef(\section3_pg, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \program,
					\voicename, [\fl,\bfl,\sx1,\sx2,\va1,\va2,\vc,\cb],
					\programname, 
						#["flute.air noise.closed.vowel varied",
						"bass.flute.jet whistle",
						"alto saxophone.slap.percussive slap",
						"alto saxophone.ordinario",
						"viola.harmonic.artificial.fourth",
						"viola.harmonic.artificial.fourth",
						"violoncello.col legno battuto.ordinario",
						"double bass.pizzicato"],
					\dur, Pn(0.01,1)
				)		 
			),
			~delays[3]+0.05,
			Pdef(\section3_fl, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pwrand([\noteon,\rest],[0.6,0.4],inf),
					\voicename, \fl,
					\midinote, 69+Pseq([0,2],inf),
					\dur, Pseq([16,8,8,8],inf),
					\legato, 0.75,
					\amp, Pexprand(0.85,1.0,inf)
				)	
			),
			~delays[3]+0.05,
			Pdef(\section3_bfl,  
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\noteon,\rest],inf)],1),
					\voicename, \bfl,
					\midinote, Prand([49,52,53,56,59,62],inf), 
					\dur, Prand([16,8,4,2],inf),
					\amp, Pexprand(0.85,1.0,inf)
				)	
			),
			~delays[3]+0.05,
			Pdef(\section3_bflpitchshift,
				Pbind(
					\instrument, \pitchshift,
					\group, ~fx,
					\in, ~master_fx_bus.subBus(1,1),
					\out, 20,
					\dur, ~durations[3],
					\atk, ~durations[3] * 0.1,
					\sus, ~durations[3] * 0.3,
					\rel, ~durations[3] * 0.8, // slight overlap with s2
					\amp, 0.7,
					\ratio, [1,1.5,2,2.5],
					\windowSize, 4.0,
					\pitchDispersion, {4.0.rand}.dup(4),
					\timeDispersion, {4.0.rand}.dup(4)
				)
			),
			~delays[3]+0.05,
			Pdef(\section3_sx1, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, \sx1,
					\midinote, Pseq([50,51],32), 
					\dur, Pseq([8,Pn(1/4,32)],inf),
					\amp, Pseq([0,Pseg(Pseq([0.2,1,0.0],1),Pseq(2!2,1))],1)
				)	
			),
			~delays[3]+0.05,
			Pdef(\section3_sx2, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\noteon,\rest],inf)],1),
					\voicename, \sx2,
					\midinote, 
					Pseq([81,
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=76}).select({|n,i| n<=86}),inf) 
						],1), 
					\dur, 16,
					\legato, Pseq([1,Pexprand(0.1,0.5,inf)],inf),
					\amp, 0.1
				)	
			),
			~delays[3]+0.05,
			Pdef(\section3_va1, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\rest,Prand([\noteon,\rest],inf)],1),
					\voicename, [\va1,\va2],
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=76}).select({|n,i| n<=86}),inf), 					\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
					\legato, Pseq([1,Pexprand(0.1,0.5,inf)],inf),
					\amp, Pexprand(0.3,0.6,inf)
				)	
			),
			~delays[3]+0.05,
			Pdef(\section3_vc, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\rest,Pn(\noteon,inf)],inf),
					\voicename, \vc,
					\midinote, 
							Pwalk(
								Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
									n>=36}).select({|n,i| n<=73}),
								Prand((1..4),inf),
								Pseq([1,-1],inf),
							0), 
					\dur, 
						Pseq([16,Prand([
							Prand([Pn(1/8,64),Pn(1/4,32),Pn(1/6,21)],1),
							Prand([16,32],1)],inf)
						],inf),
					\amp, 
						Pseq([0,
							Pif(Pbinop('<',Pkey(\dur),16),
								Pseg(Pseq([0.2,1,0.0],1),Pseq(2!4,1)),Pn(1,inf))
						],1)
				)	
			),
			~delays[3]+0.025,
			Pdef(\section3_vcwarp,
				Pbind(
					\instrument, \warp,
					\group, ~fx,
					\in, ~master_fx_bus.subBus(10,1),
					\out, 17,
					\dur, ~durations[3],
					\atk, ~durations[3] * 0.1,
					\sus, ~durations[3] * 0.3,
					\rel, ~durations[3] * 0.8, // slight overlap with s2
					\amp, 0.45,
					\warpfactor, (-5,-3..5).midiratio,
					\freqscale, Pkey(\warpfactor)
				)
			),
			~delays[3]+0.05,
			Pdef(\section3_cb, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\rest,Pn(\noteon,inf)],inf),
					\voicename, \cb,
					\midinote, 38, 
					\dur, Prand([16,8],inf),
					\legato, 0.1,
					\amp, Pexprand(0.5,1.0,inf)
				)	
			),			
			~delays[3]+0.05,
			Pdef(\section3_ctl, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \ctl,
					\midinote, 
						Ptuple([		// there must be a better way to write this...
							Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
								n>=72}).select({|n,i| n<=96}),inf),
							Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
								n>=72}).select({|n,i| n<=96}),inf),
							Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
								n>=72}).select({|n,i| n<=96}),inf),
							Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
								n>=72}).select({|n,i| n<=96}),inf),
						],1), 
					\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
					\legato, 2,
					\amp, Pexprand(0.7,1.0,inf)
				)	
			),
			~delays[3]+0.075,
			Pdef(\section3_ctlwarp,
				Pbind(
					\instrument, \warp,
					\group, ~fx,
					\in, ~master_fx_bus.subBus(12,1),
					\out, 17,
					\dur, ~durations[3],
					\atk, ~durations[3] * 0.1,
					\sus, ~durations[3] * 0.3,
					\rel, ~durations[3] * 0.8, // slight overlap with s2
					\amp, 0.45,
					\warpfactor, (-5,-3..5).midiratio,
					\freqscale, Pkey(\warpfactor)
				)
			),
			~delays[3]+0.05,
			Pdef(\section3_tt, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \tam,
					\midinote, Prand([65,66],inf),
					\dur, Pseq([Pn(16,1),Prand([4,8,16],inf)],1),
					\amp, Pexprand(0.1,0.7,inf)
				)	
			)
		], 1)
	);
	////////////////////////////////////////////////////////////////////////////////
	~sections[4] = Pfindur(~durations[4],
		Ptpar([
			~delays[4],
			Pdef(\section4_pg, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \program,
					\voicename, [\tr1,\sx1,\sx2],
					\programname, 
						#["trumpet in c.ordinario",
						"alto saxophone.ordinario",
						"alto saxophone.ordinario"],
					\dur, Pn(0.01,1)
				)		 
			),	
			~delays[4]+0.05,
			Pdef(\section4_sx1, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\noteon,\rest],inf)],1),
					\voicename, \sx1,
					\midinote, 
					Pseq([81,
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=76}).select({|n,i| n<=86}),inf) 
						],1), 
					\dur, Pn(16,inf),
					\legato, Pseq([1,Pexprand(0.1,0.5,inf)],inf),
					\amp, 0.1
				)	
			),
			~delays[4]+0.05,
			Pdef(\section4_sx2, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\noteon,\rest],inf)],1),
					\voicename, \sx2,
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=76}).select({|n,i| n<=86}),inf), 
					\dur, Pn(16,inf),
					\legato, Pseq([1,Pexprand(0.1,0.5,inf)],inf),
					\amp, 0.1
				)	
			),
			~delays[4]+0.05,
			Pdef(\section4_sxwarp,
				Pbind(
					\instrument, \warp,
					\group, ~fx,
					\in, ~master_fx_bus.subBus(6,1),
					\out, 17,
					\dur, ~durations[4],
					\atk, ~durations[4] * 0.4,
					\sus, ~durations[4] * 0.3,
					\rel, ~durations[4] * 0.3, 
					\amp, 0.45,
					\warpfactor, [-5,-3,3,5].midiratio,
					\freqscale, Pkey(\warpfactor)
				)
			),
			~delays[4]+0.05,
			Pdef(\section4_tr1, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, \tr1,
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*40.midicps}).cpsmidi.select({|n,i| 
							n>=60}).select({|n,i| n<=84}),inf), 
					\dur, Pn(32,2),
					\amp, Pexprand(0.1,0.2,inf)
				)	
			),
			~delays[4]+0.05,
			Pdef(\section4_trfreqshift,
				PmonoArtic(
					\freqshift,
					\group, ~fx,
					\in, ~master_fx_bus.subBus(4,1),
					\out, 19,
					\dur, 0.25,
					\legato, 1.0,
					\atk, ~durations[4]*0.8,
					\sus, ~durations[4]*0.01,
					\rel, ~durations[4]*0.2, 
					\amp, 1.0,
					\freq, Pseg(Pseq((80.midicps..98.midicps),inf),
						Pseq(~durations[4]/(80.midicps..98.midicps).size!(80.midicps..98.midicps).size,inf),
						1)-81.midicps
				)
			),
			~delays[4]+0.05,
			Pdef(\section4_ctl, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, \ctl,
					\midinote, 
						Ptuple([		// there must be a better way to write this...
							Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
								n>=72}).select({|n,i| n<=96}),inf),
							Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
								n>=72}).select({|n,i| n<=96}),inf),
							Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
								n>=72}).select({|n,i| n<=96}),inf),
							Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
								n>=72}).select({|n,i| n<=96}),inf),
						],1), 
					\dur, Pn(16,inf),
					\legato, 2,
					\amp, Pseg(Pseq((1.0,0.99..0.01),inf),Pn(1/8,inf),\exp,1)
				)	
			),
			~delays[4]+0.075,
			Pdef(\section4_ctlrec,
				Pbind(
					\instrument, \recbuf,
					\group, ~fx,
					\in, ~master_fx_bus.subBus(12,1),
					\dur, Pn(16,1),
					\bufnum, ~ctl_buf
				)
			),
			~delays[4]+0.05,
			Pdef(\section4_tt, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, \tam,
					\midinote, Prand([65,66],inf),
					\dur, 8,
					\amp, Pseg(Pseq((1.0,0.99..0.01),inf),Pn(1/8,inf),\exp,1)
				)	
			)
		], 1)
	);
	
	////////////////////////////////////////////////////////////////////////////////
	~sections[5] = Pfindur(~durations[5],
		Ptpar([
			~delays[5],
			Pdef(\section5_pg, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \program,
					\voicename, [\tb1],
					\programname, 
						#["tenor trombone.hit on mouthpiece"],
					\dur, Pn(0.01,1)
				)		 
			),	
			~delays[5]+0.075,
			Pdef(\section4_grain,
				Pbind(
					\instrument, \grain,
					\group, ~fx,
					\out, 21,
					\bufnum, ~ctl_buf,
					\dur, Pn(32,1),				
					\atk, Pkey(\dur)*0.0001,
					\sus, Pkey(\dur)*0.9,
					\rel, Pkey(\dur)*0.5,
					\grainfreq, 16,
					\ratehigh, 1.5,
					\ratelow, 0.75,
					\graindur, 16,
					\center, 6
				)
			),
			~delays[5]+0.05,
			Pdef(\section5_tb1, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, \tb1,
					\midinote, Pseq([45,44],1),
					\dur, 8,
					\amp, Pn(1,2)
				)	
			),
			~delays[5]+0.05,
			Pdef(\section5_ctl, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, \ctl,
					\midinote, 
						Ptuple([		// there must be a better way to write this...
							Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
								n>=72}).select({|n,i| n<=96}),inf),
							Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
								n>=72}).select({|n,i| n<=96}),inf),
							Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
								n>=72}).select({|n,i| n<=96}),inf),
							Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
								n>=72}).select({|n,i| n<=96}),inf),
						],1), 
					\dur, Pn(16,1),
					\legato, 2,
					\amp, Pseg(Pseq((1.0,0.99..0.01),inf),Pn(1/8,inf),\exp,1)
				)	
			)			
		], 1)
	);
};
~load_patterns.fork;
)