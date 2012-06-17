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
~durations[1] = 60;
~durations[2] = 32;

~pelog = Array.fill(10, {|i| Scale.pelog.degrees + (12*i) }).lace.sort;
	
~load_patterns = {
	////////////////////////////////////////////////////////////////////////////////
	~sections[0] = Pfindur(~durations[0],
		Ptpar([
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
			0.05,
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
			0.05,
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
			0.05,
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
			0.05,
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
			6,
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
			6.05,
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
			6.05,
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
			6.05,
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
			6.05,
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
			6.05,
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
			6.05,
			Pdef(\section1_recbuf,
				Pbind(
					\instrument, \recbuf,
					\group, ~fx,
					\in, ~master_fx_bus.subBus(13,1),
					\dur, Pn(16,1),
					\bufnum, ~tamtam_buf
				)
			),
			6.05,
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
			6.05,
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
			6.05,
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
			6.05,
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
		], 1)
	);
	
	////////////////////////////////////////////////////////////////////////////////
	~sections[2] = Pfindur(~durations[2],
		Ptpar([
			8,
			Pdef(\section2_pg, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \program,
					\voicename, [\fl,\bfl,\bcl,\sx1,\sx2,\cb,\vc],
					\programname, 
						#["flute.ordinario",
						"bass.flute.ordinario",
						"bass clarinet boehm system.ordinario",
						"alto saxophone.slap.percussive slap",
						"alto saxophone.multiphonic.Gubler Selmer_Super_Action_II",
						"double bass.pizzicato.bartok",
						"violoncello.flautando"],
					\dur, Pn(0.01,1)
				)		 
			),
			8.05,
			Pdef(\section2_freeze, 
				Pbind(
					\instrument, \freeze,
					\group, ~fx,
					\out, 18,
					\bufnum, ~tamtam_buf,
					\bufdur, ~tamtam_buf.duration,
					\dur, Pn(16,2),
					\cfreq, Pkey(\bufdur).reciprocal*0.5, 
					\cphase, 0,
					\cmul, Pkey(\bufdur),
					\cadd, 0,
					\atk, Pkey(\dur)*0.2,
					\sus, Pkey(\dur)*0.4,
					\rel, Pkey(\dur)*0.4, 
					\amp, Pseq([0.0,1.0],1)
				)				 
			),
			8.05,
			Pdef(\section2_fl, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \fl,
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=59}).select({|n,i| n<=96}),inf), 					\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.3,1.0,inf)
				)	
			),
			8.05,
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
			8.05,
			Pdef(\section2_bcl, 
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
			8.05,
			Pdef(\section2_sx1, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, \noteon,
					\voicename, \sx1,
					\midinote, Pseq([50,51],32), 
					\dur, Pseq([8,Pn(1/16,64)],inf),
					\amp, Pseq([0,Pseg(Pseq([0.2,1,0.0],1),Pseq(2!2,1))],1)
				)	
			),
			8.05,
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
			8.05,
			Pdef(\section2_pitchshift, 
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
			8.05,
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
			8.05,
			Pdef(\section2_vi1, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \vi1,
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=81}),inf), 							\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.7,1.0,inf)
				)	
			),
			8.05,
			Pdef(\section2_vi2, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \vi2,
					\midinote, 											Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=81}),inf), 
					\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.7,1.0,inf)
				)	
			),
			8.05,
			Pdef(\section2_vi3, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \vi3,
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=81}),inf), 							\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.7,1.0,inf)
				)	
			),
			8.05,
			Pdef(\section2_vi4, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \vi4,
					\midinote, 											Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=81}),inf), 
					\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.7,1.0,inf)
				)	
			),
			8.05,
			Pdef(\section2_va1, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \va1,
					\midinote, 
						Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=73}),inf), 							\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.7,1.0,inf)
				)	
			),
			8.05,
			Pdef(\section2_va2, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \va2,
					\midinote, 											Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n>=73}),inf), 
					\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.7,1.0,inf)
				)	
			),
			8.05,
			Pdef(\section2_vc, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \vc,
					\midinote, 											Prand(Array.fill(64,{|i| i=i+1; i*26.midicps}).cpsmidi.select({|n,i| 
							n<=84}).select({|n,i| n>=36}),inf), 
					\dur, Pseq([Pn(16,1),Prand([8,16],inf)],1),
					\amp, Pexprand(0.7,1.0,inf)
				)	
			)
		], 1)
	);
	
	////////////////////////////////////////////////////////////////////////////////
	~sections[3] = Pfindur(~durations[3],
		Ptpar([
			0.05,
			Pdef(\section3_ctl, 
				Pbind(
					\type, \ctosc, 
					\oscout, ~osc_destination,
					\osccmd, Pseq([\noteon,Prand([\rest,\noteon],inf)],1),
					\voicename, \ctl,
					\midinote, Ptuple([		// there must be a better way to write this...
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
					\amp, Pexprand(0.7,1.0,inf)
				)	
			)
		], 1)
	);
	
};
~load_patterns.fork;
)